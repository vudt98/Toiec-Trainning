package com.example.toeictraining.works

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.toeictraining.R
import com.example.toeictraining.ui.main.MainActivity
import com.example.toeictraining.utils.Constants

class RemindWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val message: String by lazy {
        inputData.getString(Constants.KEY_MESSAGE)
            ?: context.getString(R.string.content_text_daily_reminder_notification)
    }

    override fun doWork(): Result {
        createNotificationChannel()
        pushNotification()
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val priorityStatus = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, NOTIFICATION_NAME, priorityStatus)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun pushNotification() {
        val intent = MainActivity.getIntent(context)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(context.getString(R.string.content_title_daily_reminder_notification))
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setChannelId(CHANNEL_ID)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .build()
        notificationManager.notify(DEFAULT_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_NAME = "daily reminder"
        private const val DEFAULT_LIGHT_TIME = 1000L
        private const val CHANNEL_ID = "com.example.toeictraining"
        private const val DEFAULT_ID = -1
    }
}