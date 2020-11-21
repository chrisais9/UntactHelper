package kr.koohyongmo.untacthelper.common

import android.app.Application

/**
 * 앱 실행시 실행되는 클래스
 * Created by KooHyongMo on 2020/11/13
 */
class AppApplication: Application() {

    companion object {
        @Volatile
        lateinit var instance: AppApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}