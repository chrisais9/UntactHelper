package kr.koohyongmo.untacthelper.common.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import kr.koohyongmo.untacthelper.R
import kr.koohyongmo.untacthelper.common.ui.base.BaseActivity

/**
 * Created by KooHyongMo on 2020/10/03
 */
class SplashActivity: BaseActivity() {
    override val layoutResourceID: Int
        get() = R.layout.activity_splash
    override val layoutToolbarID: Int
        get() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
    }

    override fun initLayoutAttributes() {

        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }
}