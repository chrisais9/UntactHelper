package kr.koohyongmo.untacthelper.common.ui.activity

import android.app.ProgressDialog
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.widget.Toast
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.GlobalConstants
import kr.koohyongmo.untacthelper.common.data.local.sharedpreference.LoginPreference
import kr.koohyongmo.untacthelper.common.data.remote.JsoupEcampusService
import kr.koohyongmo.untacthelper.common.ui.base.BaseActivity
import org.jsoup.Jsoup

/**
 * Created by KooHyongMo on 2020/10/03
 */
class LoginActivity : BaseActivity() {
    override val layoutResourceID: Int
        get() = R.layout.activity_login
    override val layoutToolbarID: Int
        get() = 0

    private val loginPreference by lazy { LoginPreference.getInstance() }
    val ecampusService = JsoupEcampusService()

    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    private fun isSessionAlive() {
        if (loginPreference.userCookie.isNullOrEmpty())
            return
        progressDialog.setTitle("세션 확인중...")
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
                    .cookies(loginPreference.userCookie)
                    .get()

            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressDialog.dismiss() }
                .subscribe({
                    if (it.body().html().contains("로그아웃")) {
                        Log.d(TAG, "already logged in")
                        onFinishLogin()
                    }
                },{
                    it.printStackTrace()
                })
        )
    }

    override fun initLayoutAttributes() {

        isSessionAlive()

        et_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        btn_login.setOnClickListener {
            progressDialog.setTitle("로그인중...")
            progressDialog.show()
            addToDisposable(
                ecampusService.requestLogin(
                    et_id.text.toString(),
                    et_password.text.toString()
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        progressDialog.dismiss()
                        loginPreference.userID = et_id.text.toString()
                        loginPreference.userPassword = et_id.text.toString()
                        loginPreference.userCookie = it.cookies()
                        onFinishLogin()
                    }, {
                        progressDialog.dismiss()
                        Toast.makeText(this, "아이디 / 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, it.localizedMessage)
                    })
            )
        }
        if (loginPreference.userID.isNotEmpty()
            && loginPreference.userPassword.isNotEmpty()) {
            et_id.setText(loginPreference.userID)
            et_password.setText(loginPreference.userPassword)
        }
    }

    private fun onFinishLogin() {
        Log.d(TAG, loginPreference.userCookie.toString())
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}