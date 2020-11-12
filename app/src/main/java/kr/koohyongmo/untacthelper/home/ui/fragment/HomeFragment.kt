package kr.koohyongmo.untacthelper.home.ui.fragment

import android.app.ProgressDialog
import android.util.Log
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kr.koohyongmo.untacthelper.BuildConfig
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.GlobalConstants
import kr.koohyongmo.untacthelper.common.data.local.sharedpreference.LoginPreference
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import org.jsoup.Connection
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


    override fun initLayoutAttributes() {
        initTimeTable()
        fetchDataFromEcampus()
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
                },{
                    Log.d(TAG, it.localizedMessage)
                })
        )
    }

    private fun initTimeTable() {
        val schedules = arrayListOf(
            Schedule().apply {
                classTitle = "Data Structure" // sets subject
                classPlace = "IT-601" // sets place
                professorName = "Won Kim" // sets professor
                startTime = Time(10, 0) // sets the beginning of class time (hour,minute)
                endTime = Time(13, 30) // sets the end of class time (hour,minute)
            }
        )
        timetable.add(schedules)
    }

}