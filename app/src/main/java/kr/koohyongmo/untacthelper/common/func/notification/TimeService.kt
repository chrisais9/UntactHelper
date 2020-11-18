package kr.koohyongmo.untacthelper.common.func.notification

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.SystemClock
import android.util.Log
import kr.koohyongmo.untacthelper.common.ui.activity.MainActivity

/**
 * Created by KooHyongMo on 11/19/20
 */
class TimeService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d("service", "onCreate 실행")
    }

    override fun onDestroy() {
        Log.d("service", "onDestroy 실행")
        mRunning = false
    }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            NotificationHelper.notification(
                applicationContext,
                "강의 들을 시간이에요~!",
                "테스트 메세지",
                Intent(applicationContext, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }
    protected var mRunning = false

    // 제일 중요한 메서드! (서비스 작동내용을 넣어준다.)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("service", "onStartCommand 실행")
        val time = intent.getIntExtra("time", 0)

        // handler 통한 Thread 이용
        Thread(Runnable {
            mRunning = true
            while (mRunning) {
                SystemClock.sleep(time * 1000.toLong())
                mHandler.sendEmptyMessage(0)
            }
        }).start()
        return START_STICKY_COMPATIBILITY
    }
}