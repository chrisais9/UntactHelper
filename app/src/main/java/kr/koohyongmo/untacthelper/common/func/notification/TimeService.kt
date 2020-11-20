package kr.koohyongmo.untacthelper.common.func.notification

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.SystemClock
import android.text.format.DateUtils
import android.util.Log
import kr.koohyongmo.untacthelper.common.ui.activity.MainActivity
import java.lang.Math.abs
import java.util.*

/**
 * Created by KooHyongMo on 11/19/20
 */
class TimeService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
        mRunning = false
    }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            NotificationHelper.notification(
                applicationContext,
                "예정된 할일이 있어요~!",
                namesToNotify[msg.arg1],
                Intent(applicationContext, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )

            stopSelf()
        }
    }
    private var mRunning = false
    var namesToNotify = arrayListOf<String>()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val timesToNotify = intent.getStringArrayListExtra("times")!!
        namesToNotify = intent.getStringArrayListExtra("names")!!

        NotificationHelper.notification(
            applicationContext,
            "[비대리] 강의 알림",
            "강의 시간이 되면 제가 알려드릴게요~!",
            Intent(applicationContext, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )

        // handler 통한 Thread 이용
        Thread(Runnable {
            mRunning = true
            while (mRunning) {
                SystemClock.sleep(1000.toLong())

                val now = Date()
                val calendar = Calendar.getInstance()
                timesToNotify.forEachIndexed { index, it ->
                    calendar[Calendar.HOUR_OF_DAY] = it.substring(0,2).toInt()
                    calendar[Calendar.MINUTE] = it.substring(3).toInt()
                    calendar[Calendar.SECOND] = 0
                    calendar[Calendar.MILLISECOND] = 0

                    if (abs(calendar.time.time - now.time) <= 1000) {
                        val message = Message()
                        message.arg1 = index
                        mHandler.sendMessage(message)
                    }
                }
            }
        }).start()
        return START_STICKY_COMPATIBILITY
    }
}