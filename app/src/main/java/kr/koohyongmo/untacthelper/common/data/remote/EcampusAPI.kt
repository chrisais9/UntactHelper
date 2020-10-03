package kr.koohyongmo.untacthelper.common.data.remote

import io.reactivex.Single
import org.jsoup.Connection
import org.jsoup.nodes.Document

/**
 * Created by KooHyongMo on 2020/10/03
 */
interface EcampusAPI {

    fun requestLogin(
        id: String,
        password: String
    ) : Single<Connection.Response>

//    fun checkLoginStatus(
//        token: String
//    ) : Single<Connection.Response>
}