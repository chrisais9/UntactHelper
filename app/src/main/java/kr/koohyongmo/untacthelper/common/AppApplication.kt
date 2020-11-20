package kr.koohyongmo.untacthelper.common

import android.app.Application
import io.realm.Realm

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

        Realm.init(this)
    }
}