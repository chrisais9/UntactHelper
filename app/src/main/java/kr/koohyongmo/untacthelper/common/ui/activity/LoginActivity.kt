package kr.koohyongmo.untacthelper.common.ui.activity

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.data.local.sharedpreference.LoginPreference
import kr.koohyongmo.untacthelper.common.data.remote.JsoupEcampusService
import kr.koohyongmo.untacthelper.common.ui.base.BaseActivity

/**
 * Created by KooHyongMo on 2020/10/03
 */
class LoginActivity: BaseActivity() {
    override val layoutResourceID: Int
        get() = R.layout.activity_login
    override val layoutToolbarID: Int
        get() = 0

    private val loginPreference by lazy { LoginPreference.getInstance(this) }

    override fun initLayoutAttributes() {
        val ecampusService = JsoupEcampusService()
        btn_login.setOnClickListener {
            addToDisposable(
                ecampusService.requestLogin(
                    et_id.text.toString(),
                    et_password.text.toString()
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        loginPreference.userID = et_id.text.toString()
                        loginPreference.userPassword = et_id.text.toString()
                        loginPreference.userCookie = it.cookies()
                        Log.d(TAG, loginPreference.userCookie.toString())
                    },{
                        Log.d(TAG, it.localizedMessage)
                    })
            )
        }
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}