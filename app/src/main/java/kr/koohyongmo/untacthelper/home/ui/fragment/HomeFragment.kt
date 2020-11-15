package kr.koohyongmo.untacthelper.home.ui.fragment

import android.app.ProgressDialog
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
import kr.koohyongmo.untacthelper.common.data.local.ecampus.LectureType
import kr.koohyongmo.untacthelper.common.data.local.sharedpreference.LoginPreference
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import kr.koohyongmo.untacthelper.databinding.ItemHomeTodoBinding
import kr.koohyongmo.untacthelper.home.viewmodel.TodoViewModel
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
    private var todoList = ArrayList<TodoViewModel>()
    private val todoAdapter by lazy {
        LastAdapter(todoList, BR.listContent)
    }
    override fun initLayoutAttributes() {
        initTodo()
        fetchDataFromEcampus()
        initTodoItem()
    }

    private fun fetchDataFromEcampus() {
        val userAgent =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_16_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36"

        progressDialog.show()
        addToDisposable(
            Single.fromCallable {
                Jsoup.connect(GlobalConstants.ECAMPUS_MAIN_URL)
                    .userAgent(userAgent)
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
                    val courses =
                        document.body().select(".progress_courses .course_lists .my-course-lists")
                    courses.select("div.course-title").forEach {
                        Log.d(TAG, it.html())
                    }
                }, {
                    Log.d(TAG, it.localizedMessage)
                })
        )
    }

    private fun initTodo() {
        rv_todo.layoutManager = LinearLayoutManager(requireContext())
        todoAdapter
            .map<TodoViewModel,ItemHomeTodoBinding>(R.layout.item_home_todo) {
                onBind {
                    Log.d(TAG, "onBind")
                }
            }
            .into(rv_todo)
    }

    private fun initTodoItem() {
        todoList.addAll(
            listOf(
                TodoViewModel(
                    "11:10",
                    "응용통계학",
                    LectureType.TYPE_VIDEO,
                    "[강의동영상] 11장 범주형 자료"
                ),
                TodoViewModel(
                    "13:50",
                    "컴퓨터구조",
                    LectureType.TYPE_VIDEO,
                    "11/06 화상강의"
                ),
                TodoViewModel(
                    "23:59",
                    "이산수학",
                    LectureType.TYPE_VIDEO,
                    "중간고사 대체과제"
                )
            )
        )
        todoAdapter.notifyDataSetChanged()
    }

}