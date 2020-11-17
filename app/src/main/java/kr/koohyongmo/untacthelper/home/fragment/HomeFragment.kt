package kr.koohyongmo.untacthelper.home.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
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
import kr.koohyongmo.untacthelper.databinding.ItemHomeFutureTodoBinding
import kr.koohyongmo.untacthelper.databinding.ItemHomeTodayTodoBinding
import kr.koohyongmo.untacthelper.home.viewmodel.FutureTodoViewModel
import kr.koohyongmo.untacthelper.home.viewmodel.TodayTodoViewModel
import org.jsoup.Jsoup

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

                    links.forEachIndexed { classIndex, link ->
                        if (classIndex == 1) fetchLectureData(classIndex, link, classNames[classIndex])
                    }
                }, {
                    Log.d(TAG, it.localizedMessage)
                })
        )
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


                    it.body().select(".total_sections .weeks.ubsweeks li .content")
                        .forEachIndexed { index, contents ->
                            if (contents.select(".sectionname").text()[0].isDigit()) {

//                            Log.d(TAG, contents.select(".sectionname").text().substring(0,2))
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
                                    val mode = when (section.select("img")
                                        .attr("alt")) {
                                        "화상강의" -> LectureType.TYPE_ZOOM
                                        "콘텐츠제작도구" -> LectureType.TYPE_VIDEO
                                        "과제" -> LectureType.TYPE_ASSIGNMENT
                                        else -> LectureType.TYPE_FILE
                                    }

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
                                    EcampusCacheUtil.mEcampusMain.classes[classIndex].week[weekNumber].lectures.add(
                                        Lecture(
                                            title,
                                            mode,
                                            dueStart,
                                            dueEnd
                                        )
                                    )
                                }
//                                if (EcampusCacheUtil.mEcampusMain.classes[classIndex].week[weekNumber].lectures.isNotEmpty())
//                                    EcampusCacheUtil.mEcampusMain.classes[classIndex].week[weekNumber].lectures.sortedBy { lecture ->
//                                        lecture.dueEnd
//                                    }
                            }

                        }
                    futureTodoList.clear()
                    EcampusCacheUtil.mEcampusMain.classes[classIndex].week.forEach { week ->
                        week.lectures.forEach { lecture ->
                            if (lecture.dueEnd.isNotEmpty()) {
                                futureTodoList.add(
                                    FutureTodoViewModel(
                                        lecture.dueEnd,
                                        className,
                                        lecture.type,
                                        lecture.title,
                                        ""
                                    )
                                )
                            }
                        }
                    }
                    futureTodoAdapter.notifyDataSetChanged()
                }, {
                    Log.d(TAG, it.localizedMessage)
                })
        )
    }

    private fun initTodo() {
        rv_today_todo.layoutManager = LinearLayoutManager(requireContext())
        todayTodoAdapter
            .map<TodayTodoViewModel, ItemHomeTodayTodoBinding>(R.layout.item_home_today_todo) {
                onClick {
                    val redirectLink = it.binding.listContent!!.contentURL
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redirectLink)))
                }
            }
            .into(rv_today_todo)

        rv_future_todo.layoutManager = LinearLayoutManager(requireContext())
        futureTodoAdapter
            .map<FutureTodoViewModel, ItemHomeFutureTodoBinding>(R.layout.item_home_future_todo) {
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
                        val classTitle = element.select(".course").text()
                        val contentTitle = element.select(".referer a").text()
                        val link = element.select(".referer a").attr("href")
                        var startTime = ""
                        if (element.select(".date").text().length <= 13) {
                            startTime = element.select(".date").text()
                            if (startTime.isNotEmpty()) startTime =
                                startTime.substring(0, 5) // 10:25
                        }
                        if (startTime.isEmpty()) return@forEachIndexed
                        todayTodoList.add(
                            TodayTodoViewModel(
                                startTime,
                                classTitle,
                                LectureType.TYPE_VIDEO,
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