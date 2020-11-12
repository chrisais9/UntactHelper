package kr.koohyongmo.untacthelper.common

import android.app.Application

/**
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