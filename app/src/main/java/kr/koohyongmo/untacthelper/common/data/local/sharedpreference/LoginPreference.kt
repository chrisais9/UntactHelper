package kr.koohyongmo.untacthelper.common.data.local.sharedpreference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * Created by KooHyongMo on 2020/10/03
 */
class LoginPreference private constructor(context: Context){

    companion object {

        // Instance 생성
        @SuppressLint("StaticFieldLeak")
        private var instance: LoginPreference? = null

        fun getInstance(context: Context): LoginPreference {
            if (instance == null) instance = LoginPreference(context)
            return instance as LoginPreference
        }

        const val PREF_LOGIN = "preference_login"
        const val USER_ID = "login_user_id"
        const val USER_PASSWORD = "login_user_password"
        const val USER_LOGIN_COOKIE = "login_user_login_cookie"
    }
    private val pref: SharedPreferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
    private val edit = pref.edit()

    var userID: String
        get() = pref.getString(USER_ID,"").toString()
        set(v) {
            edit.putString(USER_ID, v)
            edit.apply()
        }

    var userPassword: String
        get() = pref.getString(USER_PASSWORD,"").toString()
        set(v) {
            edit.putString(USER_PASSWORD, v)
            edit.apply()
        }

    var userCookie: Map<String, String>
        get() {
            val raw = pref.getString(USER_LOGIN_COOKIE, "").toString()
            val type = object : TypeToken<HashMap<String, String>>() {}.type
            return Gson().fromJson(raw, type)
        }
        set(v) {
            edit.putString(USER_LOGIN_COOKIE, Gson().toJson(v))
            edit.apply()
        }
}