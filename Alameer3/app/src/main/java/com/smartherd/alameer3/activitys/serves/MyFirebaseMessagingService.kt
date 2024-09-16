package com.smartherd.alameer3.activitys.serves

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.common.base.MoreObjects.ToStringHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.MainActivity

const val chanalid = "fcm_default_channel"
const val chanName ="com.smartherd.alameer3"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        Log.i("SellerFirebaseService ", "Refreshed token :: $token")

//        sendRegistrationToServer(token)
    }
//    private fun sendRegistrationToServer(token: String) {
//        // TODO : send token to tour server
//    }
    override fun onMessageReceived(message: RemoteMessage) {
//        if(message.data.isNotEmpty()){
//            val titalee = message.data["tital"]
//            val booody = message.data["body"]
//            qenretNv(titalee!!,booody!!)
//
//
//        }

        if(message.notification != null){

            try {
                qenretNv(message.notification!!.title!!,message.notification!!.body!!,message.notification!!.imageUrl)
            }catch (ee:Exception){
                 val handler = Handler(Looper.getMainLooper())
                handler.post {
                    // عرض الإشعار هنا في الخيط الرئيسي
                    qenretNv(message.notification!!.title!!,message.notification!!.body!!,message.notification!!.imageUrl)

                }
            }

        }
    }
    private fun isAppInForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val appProcesses = activityManager?.runningAppProcesses
        appProcesses?.let {
            for (appProcess in it) {
                if (appProcess.processName == packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true
                }
            }
        }
        return false
    }


    fun qenretNv(tital_nav:String, contint_nav:String,img: Uri?){

//        try {

            val intent = Intent(this, MainActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val padin = PendingIntent.getActivities(this,0, arrayOf(intent), PendingIntent.FLAG_IMMUTABLE)
            var bilder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,chanalid)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_squer)
                .setVibrate(longArrayOf(1000,1000,1000,1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(padin)

            bilder = bilder.setContent(getReso(tital_nav,contint_nav,img))

            val novMAnger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val novChanal = NotificationChannel(chanalid, chanName, NotificationManager.IMPORTANCE_HIGH)
                novMAnger.createNotificationChannel(novChanal)

            }
            novMAnger.notify(0,bilder.build())

//        }catch (ee:Exception){
//            Toast.makeText(this,"تمت مراجعة طلبك الجديد",Toast.LENGTH_LONG).show()
//        }

//        val builder: NotificationCompat.Builder = if (isAppInForeground()) {
//            // التطبيق مفتوح في الأمامية، قم بإنشاء إشعار منبثق
//            NotificationCompat.Builder(applicationContext, chanalid)
//                .setAutoCancel(true)
//                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
//                .setOnlyAlertOnce(true)
//                .setFullScreenIntent(padin, true)
//                .setContent(getReso(tital_nav, contint_nav, img))
//        } else {
//            // التطبيق غير مفتوح في الأمامية، قم بإنشاء إشعار عادي
//            NotificationCompat.Builder(applicationContext, chanalid)
//                .setAutoCancel(true)
//                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
//                .setOnlyAlertOnce(true)
//                .setContentIntent(padin)
//                .setContent(getReso(tital_nav, contint_nav, img))
//        }
//        val novManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val novChannel = NotificationChannel(chanalid, chanName, NotificationManager.IMPORTANCE_HIGH)
//            novManager.createNotificationChannel(novChannel)
//        }
//
//        novManager.notify(0, builder.build())






    }

    @SuppressLint("RemoteViewLayout")
    private fun getReso(titalNav: String, contintNav: String,img: Uri?): RemoteViews? {
        val remote = RemoteViews("com.smartherd.alameer3",R.layout.navightion)
//        try {



            remote.setTextViewText(R.id.tital_nav,titalNav)
            remote.setTextViewText(R.id.contint_nav,contintNav)
            if(img!=null){
                remote.setImageViewUri(R.id.imge_nave,img)

            }

//        }catch (ee:Exception){
            Toast.makeText(this,"تمت مراجعة طلبك الجديد",Toast.LENGTH_LONG).show()
//        }

        return remote

    }

}