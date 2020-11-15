package kr.koohyongmo.untacthelper.common.ui.activity

import android.app.ProgressDialog
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.widget.Toast
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
class LoginActivity : BaseActivity() {
    override val layoutResourceID: Int
        get() = R.layout.activity_login
    override val layoutToolbarID: Int
        get() = 0

    private val loginPreference by lazy { LoginPreference.getInstance() }

    private val progressDialog by lazy {
        ProgressDialog(this).apply {
            setTitle("로그인중...")
        }
    }

    override fun initLayoutAttributes() {
        val ecampusService = JsoupEcampusService()

        et_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        btn_login.setOnClickListener {
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
        if (!loginPreference.userCookie.isNullOrEmpty()) {
            onFinishLogin()
            return
        }
        if (loginPreference.userID.isNotEmpty()
            && loginPreference.userPassword.isNotEmpty()) {
            et_id.setText(loginPreference.userID)
            et_password.setText(loginPreference.userPassword)
            btn_login.performClick()
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