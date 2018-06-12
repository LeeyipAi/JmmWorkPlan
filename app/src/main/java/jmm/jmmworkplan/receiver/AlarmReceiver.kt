package jmm.jmmworkplan.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import jmm.jmmworkplan.R
import jmm.jmmworkplan.ui.activity.SplashActivity


class AlarmReceiver : BroadcastReceiver() {
    internal var INTENT_ALARM_LOG = "intent_alarm_log"
    private lateinit var builder: NotificationCompat.Builder
    private lateinit var notification: Notification.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var mContext: Context
    private lateinit var pendingIntent: PendingIntent

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        mContext = context
        if (action == INTENT_ALARM_LOG) {
            initNotification()
        }

    }

    private fun initNotification() {
        val intent = Intent(mContext,
                SplashActivity::class.java)
        pendingIntent = PendingIntent.getActivity(
                mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel("channel", "channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            notification = getChannelNotification("金买卖工作计划", "立即去填写当天工作计划")
            notificationManager.notify(1000, notification.build())
        } else {
            builder = NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_logo)
                    .setContentText("立即去填写当天工作计划")
                    .setContentTitle("金买卖工作计划")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
            notificationManager.notify(1000, builder.build())
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getChannelNotification(title: String, content: String): Notification.Builder {
        return Notification.Builder(mContext, "channel")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_logo)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
    }
}