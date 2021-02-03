package com.avion.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.avion.app.repository.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {


        Log.d(TAG, "From: ${remoteMessage?.from}")
        val data: Map<String, String> = remoteMessage!!.data
        if (data.containsKey("orderID")) {
            Log.d(TAG, "NOTIF DATA RECEIVED!!")

        }


        remoteMessage.data?.isNotEmpty()?.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            if (true) {

                scheduleJob()
            } else {

                handleNow()
            }
        }


        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.body!!, remoteMessage.data?.get("orderID")!!)
        }


    }


    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")




        sendRegistrationToServer(token)
    }


    private fun scheduleJob() {

        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance().beginWith(work).enqueue()

    }


    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }


    private fun sendRegistrationToServer(token: String?) {
        UserRepository().refisterFirebase(token)
    }


    private fun sendNotification(messageBody: String, orderID: String) {
        val arg = Bundle()
        arg.putString("order_id", orderID)
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.main_nav)
                .setDestination(R.id.orderInfoFragment)
                .setArguments(arg)
                .createPendingIntent()


        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val CHANNEL_ID = getString(R.string.default_notification_channel_id)
        val name = getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel: NotificationChannel = NotificationChannel("ch_n", name, importance)
        mChannel.enableVibration(true)
        mChannel.description = messageBody
        mChannel.vibrationPattern = LongArray(5, { 1000 })
        mChannel.lightColor = Color.YELLOW
        val notification: Notification = NotificationCompat.Builder(this, "ch_n")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(resources.getColor(R.color.white))
                .setContentTitle(name)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(messageBody)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(importance)
                .setVibrate(LongArray(5, { 1000 }))
                .setContentIntent(pendingIntent)
                .setChannelId("ch_n")
                .build()


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        notificationManager.createNotificationChannel(mChannel)
        notificationManager.notify(1, notification)


        val notificationManagerr = this.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("ch_n", name,
                    NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.vibrationPattern = longArrayOf(300, 300, 300)
            if (defaultSoundUri != null) {
                val att = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                notificationChannel.setSound(defaultSoundUri, att)
            }
            notificationManagerr.createNotificationChannel(notificationChannel)
            notificationBuilder = NotificationCompat.Builder(this.applicationContext, notificationChannel.id)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(resources.getColor(R.color.white))
                    .setContentTitle(name + "2")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentText(messageBody)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(importance)
                    .setVibrate(LongArray(5, { 1000 }))
                    .setContentIntent(pendingIntent)
                    .setChannelId("ch_n")


        }
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}