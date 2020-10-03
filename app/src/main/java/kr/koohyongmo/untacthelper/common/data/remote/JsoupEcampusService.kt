package kr.koohyongmo.untacthelper.common.data.remote

import android.util.Log
import io.reactivex.Single
import kr.koohyongmo.untacthelper.BuildConfig
import kr.koohyongmo.untacthelper.common.GlobalConstants
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.w3c.dom.Document

/**
 * Created by KooHyongMo on 2020/10/03
 */
class JsoupEcampusService: EcampusAPI {

    private val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_16_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36"

    override fun requestLogin(id: String, password: String): Single<Connection.Response> {
        return Single.fromCallable {
            // 쿠키 받아오기
            Jsoup.connect(GlobalConstants.ECAMPUS_LOGIN_URL)
                .userAgent(userAgent)
                .data("Referer", "https://ecampus.kookmin.ac.kr/login.php")
                .data("Origin", "https://ecampus.kookmin.ac.kr")
                .data("Content-Type", "application/x-www-form-urlencoded")
                .data("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .method(Connection.Method.GET)
                .execute()
        }.flatMap {
            Single.fromCallable {
                val form = hashMapOf(
                    "username" to id,
                    "password" to password
                )

                Jsoup.connect(GlobalConstants.ECAMPUS_LOGIN_URL)
                    .userAgent(userAgent)
                    .data("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .data("Host", "ecampus.kookmin.ac.kr")
                    .data("Accept-Language", "ko-kr")
                    .data("Accept-Encoding", "gzip, deflate, br")
                    .data("Connection", "keep-alive")
                    .data(form)
                    .method(Connection.Method.POST)
                    .execute()
            }
        }
    }

//    override fun checkLoginStatus(token: String): Single<Connection.Response> {
//    }
}