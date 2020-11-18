package kr.koohyongmo.untacthelper.home.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.text.format.DateUtils
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kr.koohyongmo.untacthelper.BR
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.GlobalConstants
import kr.koohyongmo.untacthelper.common.data.local.ecampus.*
import kr.koohyongmo.untacthelper.common.data.local.sharedpreference.LoginPreference
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import kr.koohyongmo.untacthelper.databinding.ItemFutureTodoHeaderBinding
import kr.koohyongmo.untacthelper.databinding.ItemHomeFutureTodoBinding
import kr.koohyongmo.untacthelper.databinding.ItemHomeTodayTodoBinding
import kr.koohyongmo.untacthelper.home.viewmodel.FutureTodoViewModel
import kr.koohyongmo.untacthelper.home.viewmodel.TodayTodoViewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by KooHyongMo on 2020/09/19
 */
class HomeFragment : BaseFragment() {

    companion object {
        const val TAG = "HomeFragment"
    }

    override val layoutResourceID: Int
        get() = R.layout.fragment_home
    override val layoutToolbarID: Int
        get() = R.id.toolbar

    val loginPref by lazy { LoginPreference.getInstance() }
    val progressDialog by lazy {
        ProgressDialog(requireContext()).apply {
            setTitle("데이터 로드중")
        }
    }
    private var todayTodoList = ArrayList<Any>()
    private val todayTodoAdapter by lazy {
        LastAdapter(todayTodoList, BR.listContent)
    }

    private var futureTodoList = ArrayList<Any>()
    private val futureTodoAdapter by lazy {
        LastAdapter(futureTodoList, BR.listContent)
    }

    override fun initLayoutAttributes() {

        // 툴바 현재 날짜로 설정
        toolbar_text.text = DateUtils.formatDateTime(
            requireContext(),
            Date().time,
            DateUtils.FORMAT_SHOW_DATE
                    or DateUtils.FORMAT_NO_YEAR
                    or DateUtils.FORMAT_SHOW_WEEKDAY
        )

        initTodo()
        fetchDataFromEcampus()
        initTodoItem()
    }

    private fun fetchDataFromEcampus() {
        progressDialog.show()
        addToDisposable(
            Single.fromCallable {
                Jsoup.connect(GlobalConstants.ECAMPUS_MAIN_URL)
                    .userAgent(GlobalConstants.USERAGENT)
                    .data(
                        "Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
                    )
                    .data("Host", "ecampus.kookmin.ac.kr")
                    .data("Accept-Language", "ko-kr")
                    .data("Accept-Encoding", "gzip, deflate, br")
                    .data("Connection", "keep-alive")
                    .cookies(loginPref.userCookie)
                    .get()

            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressDialog.dismiss() }
                .subscribe({ document ->
                    Log.d(TAG, document.html())
                    parseEcampusMain(document)
                }, {
                    Log.d(TAG, it.localizedMessage)
                })
        )
    }

    private fun parseEcampusMain(document: Document) {
        EcampusCacheUtil.mEcampusMain = EcampusMain()

        // 수강 목록 가져오기
        val courses =
            document.body().select(".progress_courses .course_lists .my-course-lists")

        // 과목명, 교수명 가져오기
        val classNames = mutableListOf<String>()
        val professors = mutableListOf<String>()
        courses.select("div.course-title").forEachIndexed { index, it ->
            val className = it.text().substring(0, it.text().lastIndexOf(")") + 1)
            classNames.add(className)

            var professor = it.text().substring(it.text().lastIndexOf(")") + 1)
            if (professor.lastIndexOf(" ") != -1)
                professor = professor.substring(professor.lastIndexOf(" "))
            // 교수 이름이 존재 하지 않을때 처리
            if (professor.isNotEmpty())
                professors.add(professor.trim())
            else
                professors.add(" ")
        }

        // 수강과목별 하이퍼링크 가져오기
        val links = courses.select(".course_link").map {
            it.attr("href")
        }

        // static 객체에 넣어줌
        classNames.forEachIndexed { index, s ->
            EcampusCacheUtil.mEcampusMain.classes.add(
                Class(s, professors[index], links[index])
            )
        }

        // 과목 링크별로 내부 강의 파싱 시작
        futureTodoList.clear()
        links.forEachIndexed { classIndex, link ->
            fetchLectureData(classIndex, link, classNames[classIndex])
        }
    }

    private fun fetchLectureData(classIndex: Int, classUrl: String, className: String) {
        addToDisposable(
            Single.fromCallable {
                Jsoup.connect(classUrl)
                    .userAgent(GlobalConstants.USERAGENT)
                    .data(
                        "Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
                    )
                    .data("Host", "ecampus.kookmin.ac.kr")
                    .data("Accept-Language", "ko-kr")
                    .data("Accept-Encoding", "gzip, deflate, br")
                    .data("Connection", "keep-alive")
                    .cookies(loginPref.userCookie)
                    .get()

            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    parseLectureData(it, classIndex, className)
                }, {
                    Log.d(TAG, it.localizedMessage)
                })
        )
    }

    private fun parseLectureData(document: Document, classIndex: Int, className: String) {
        document.body().select(".total_sections .weeks.ubsweeks li .content")
            .forEach { contents ->
                if (contents.select(".sectionname").text()[0].isDigit()) {

                    // N 주차
                    val weekNumber = if (contents.select(".sectionname").text()
                            .substring(0, 2)[0].isDigit()
                        && contents.select(".sectionname").text()
                            .substring(0, 2)[1].isDigit()
                    ) {
                        contents.select(".sectionname").text().substring(0, 2).toInt()
                    } else {
                        contents.select(".sectionname").text().substring(0, 1).toInt()
                    } - 1

                    EcampusCacheUtil.mEcampusMain.classes[classIndex].week.add(Week())
                    EcampusCacheUtil.mEcampusMain.classes[classIndex].week[weekNumber].lectures.clear()

                    contents.select(".section.img-text li").forEach { section ->
                        val title =
                            section.select(".instancename").removeClass(".accesshide")
                                .text()

                        // 강의 타입
                        val mode = when (section.select("img")
                            .attr("alt")) {
                            "화상강의" -> LectureType.TYPE_ZOOM
                            "콘텐츠제작도구" -> LectureType.TYPE_VIDEO
                            "과제" -> LectureType.TYPE_ASSIGNMENT
                            else -> LectureType.TYPE_FILE
                        }

                        // 강의별 링크
                        val link = section.select("a").attr("href")

                        // 시작 / 마감 기한
                        val due = section.select(".displayoptions").text()
                        var dueStart = ""
                        var dueEnd = ""
                        if (due.length >= 41) {
                            dueStart = due.substring(0, 19)
                            if (dueStart.substring(0, 2) != "20")
                                dueStart = ""
                            dueEnd = due.substring(22, 41)
                            if (dueEnd.substring(0, 2) != "20")
                                dueEnd = ""
                        }

                        // static 객체에 최종적으로 넣어줌
                        EcampusCacheUtil.mEcampusMain.classes[classIndex].week[weekNumber].lectures.add(
                            Lecture(
                                title,
                                mode,
                                dueStart,
                                dueEnd,
                                link
                            )
                        )
                    }
                }

            }

        // 날짜 같은 항목들은 합치기 위한 로직을 위한 변수
        var prevTime = 0L

        // 이캠퍼스 과목 > 주차별 강의
        EcampusCacheUtil.mEcampusMain.classes[classIndex].week.forEach { week ->
            week.lectures.forEach { lecture ->

                // 마감기한이 있는 강의(과제)
                if (lecture.dueEnd.isNotEmpty()) {
                    val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val itemTime = parser.parse(lecture.dueEnd)!!.time
                    if (Date().time <= itemTime) {

                        // 날짜가 이전 항목과 다르면 날짜 UI에 표시
                        if (prevTime != itemTime) {
                            prevTime = itemTime
                            futureTodoList.add(
                                DateUtils.formatDateTime(
                                    requireContext(),
                                    itemTime,
                                    DateUtils.FORMAT_SHOW_DATE
                                            or DateUtils.FORMAT_NO_YEAR
                                            or DateUtils.FORMAT_SHOW_WEEKDAY
                                )
                            )
                        }
                        futureTodoList.add(
                            FutureTodoViewModel(
                                "~${lecture.dueEnd.substring(11, 16)}", // 2020-11-18 23:59 -> ~23:59
                                className,
                                lecture.type,
                                lecture.title,
                                lecture.link
                            )
                        )
                    }
                }
            }
        }
        futureTodoAdapter.notifyDataSetChanged()
    }

    private fun initTodo() {
        rv_today_todo.layoutManager = LinearLayoutManager(requireContext())
        todayTodoAdapter
            .map<TodayTodoViewModel, ItemHomeTodayTodoBinding>(R.layout.item_home_today_todo) {
                onBind {
                    when(it.binding.listContent!!.contentType) {
                        LectureType.TYPE_FILE -> {
                            it.binding.ivType.setImageResource(R.drawable.ic_attachment)
//                            it.binding.ivType.setColorFilter(Color.parseColor("#ff3131"), PorterDuff.Mode.MULTIPLY)
                        }
                        LectureType.TYPE_VIDEO -> {
                            it.binding.ivType.setImageResource(R.drawable.ic_video)
                            it.binding.ivType.setColorFilter(Color.parseColor("#ff3131"), PorterDuff.Mode.MULTIPLY)
                        }
                        LectureType.TYPE_ZOOM -> {
                            it.binding.ivType.setImageResource(R.drawable.ic_zoom)
                            it.binding.ivType.setColorFilter(Color.parseColor("#2390dc"), PorterDuff.Mode.MULTIPLY)
                        }
                        LectureType.TYPE_ASSIGNMENT -> {
                            it.binding.ivType.setImageResource(R.drawable.ic_assingment)
                            it.binding.ivType.setColorFilter(Color.parseColor("#26df44"), PorterDuff.Mode.MULTIPLY)
                        }
                    }
                }
                onClick {
                    val redirectLink = it.binding.listContent!!.contentURL
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redirectLink)))
                }
            }
            .into(rv_today_todo)

        rv_future_todo.layoutManager = LinearLayoutManager(requireContext())
        futureTodoAdapter
            .map<String, ItemFutureTodoHeaderBinding>(R.layout.item_future_todo_header)
            .map<FutureTodoViewModel, ItemHomeFutureTodoBinding>(R.layout.item_home_future_todo) {
                onBind {
                    when(it.binding.listContent!!.contentType) {
                        LectureType.TYPE_FILE -> {
                            it.binding.ivType.setImageResource(R.drawable.ic_attachment)
                        }
                        LectureType.TYPE_VIDEO -> {
                            it.binding.ivType.setImageResource(R.drawable.ic_video)
                        }
                        LectureType.TYPE_ZOOM -> {
                            it.binding.ivType.setImageResource(R.drawable.ic_zoom)
                        }
                        LectureType.TYPE_ASSIGNMENT -> {
                            it.binding.ivType.setImageResource(R.drawable.ic_assingment)
                        }
                    }
                }
                onClick {
                    val redirectLink = it.binding.listContent!!.contentURL
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redirectLink)))
                }
            }
            .into(rv_future_todo)
    }

    private fun initTodoItem() {

        todayTodoList.clear()
        addToDisposable(
            Single.fromCallable {
                Jsoup.connect(GlobalConstants.ECAMPUS_CALENDAR_URL)
                    .userAgent(GlobalConstants.USERAGENT)
                    .data(
                        "Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
                    )
                    .data("Host", "ecampus.kookmin.ac.kr")
                    .data("Accept-Language", "ko-kr")
                    .data("Accept-Encoding", "gzip, deflate")
                    .data("Connection", "keep-alive")
                    .cookies(loginPref.userCookie)
                    .get()
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ document ->
                    val events = document.select(".eventlist").first().children()
                    events.forEachIndexed { index, element ->
                        // 강의 제목
                        val classTitle = element.select(".course").text()

                        // 세부 사항
                        val contentTitle = element.select(".referer a").text()

                        // 클릭 링크
                        val link = element.select(".referer a").attr("href")

                        // 시작시간
                        var startTime = ""
                        if (element.select(".date").text().length <= 13) {
                            startTime = element.select(".date").text()
                            if (startTime.isNotEmpty()) startTime =
                                startTime.substring(0, 5) // 10:25
                        }

                        // 강의 타입
                        val mode = when (element.select("img")
                            .attr("title")) {
                            "화상강의" -> LectureType.TYPE_ZOOM
                            "콘텐츠제작도구" -> LectureType.TYPE_VIDEO
                            "과제" -> LectureType.TYPE_ASSIGNMENT
                            else -> LectureType.TYPE_FILE
                        }

                        if (startTime.isEmpty()) return@forEachIndexed
                        todayTodoList.add(
                            TodayTodoViewModel(
                                startTime,
                                classTitle,
                                mode,
                                contentTitle,
                                link
                            )
                        )

                    }
                    todayTodoAdapter.notifyDataSetChanged()
                }, {

                })
        )
    }

}