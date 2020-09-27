package kr.koohyongmo.untacthelper.home.ui.fragment

import android.util.Log
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kr.koohyongmo.untacthelper.BuildConfig
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.GlobalConstants
import kr.koohyongmo.untacthelper.common.ui.base.BaseFragment
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.File
import java.io.FileInputStream
import java.util.*

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


    override fun initLayoutAttributes() {
        initTimeTable()
        establishEcampusConnection()
    }

    private fun establishEcampusConnection() {

        val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15"

        addToDisposable(
            Single.fromCallable {
                // 쿠키 받아오기
                Jsoup.connect(GlobalConstants.ECAMPUS_LOGIN_URL)
                    .userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .execute()
            }.flatMap {
                Single.fromCallable {
                    val form = hashMapOf(
                        "username" to BuildConfig.ecampusID,
                        "password" to BuildConfig.ecampusPassword
                    )

                    Jsoup.connect(GlobalConstants.ECAMPUS_LOGIN_URL)
                        .userAgent(userAgent)
                        .data(form)
                        .cookies(it.cookies())
                        .method(Connection.Method.POST)
                        .execute()
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, it.body())

//                    Log.d(TAG, it.body().substring(0,10))
                },{
                    Log.d(TAG, it.localizedMessage)
                })

        )
//        addToDisposable(
//            Single.fromCallable {
//                Jsoup.connect(GlobalConstants.ECAMPUS_LOGIN_URL)
//                    .header("Referer", "https://ecampus.kookmin.ac.kr/login.php")
//                    .header("Origin","https://ecampus.kookmin.ac.kr")
//                    .header("Accept-Language","ko-kr")
//                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//                    .header("User-Agent", userAgent)
//                    .header("Accept-Encoding","gzip, deflate, br")
//                    .method(Connection.Method.GET)
//                    .execute()
//            }.subscribeOn(Schedulers.io())
//                .flatMap {
//                    Single.fromCallable {
//                        val loginPageCookies = it.cookies()
//                        val loginPageDocument = it.parse()
//
//                        val form = hashMapOf(
//                            "username" to "20191551",
//                            "password" to "@hmk100300",
//                            "rememberusename" to loginPageDocument.select("rememberusename").`val`()
//                        )
//                        it
//                    }
//                }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    Log.d(TAG, it.statusCode().toString())
//                },{
//
//                })
//
//        )
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