package kr.koohyongmo.untacthelper.common.func.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kr.koohyongmo.untacthelper.R
import java.util.*

/**
 * Created by KooHyongMo on 11/19/20
 */
object NotificationHelper {
    fun notification(context: Context, title: String, message: String, intent: Intent, channelId: String = "notice", channelName: String = "Notice") {

        val randId = ((Date().time / 1000L) % Int.MAX_VALUE).toInt()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context, randId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = ContextCompat.getColor(context, R.color.colorPrimary)
            notificationChannel.enableVibration(false)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
            notificationManager.createNotificationChannel(notificationChannel)

            val builder = Notification.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_main)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            notificationManager.notify(randId, builder.build())
        } else {
            val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_main)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))

            if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(LongArray(0))

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(randId, notificationBuilder.build())
        }
    }
}