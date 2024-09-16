package com.smartherd.alameer3.activitys.Helpers

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.smartherd.alameer3.activitys.activty.LoginSiginpActivity
import com.smartherd.alameer3.activitys.models.UserDate
import com.smartherd.alameer3.activitys.serves.UserServes

class ChickerFunction {

    fun changeStatusBarColor(color: String,window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = android.graphics.Color.parseColor(color)
        }
    }


    fun chickUser(context: Context , callback: (UserDate?) -> Unit){
        var userId = UserServes().getUserID()
        if(userId.isNotEmpty()){

            UserServes().getUserData(userId) { userDate ->
                if (userDate != null) {
                    callback(userDate)
                }
                else{
                    Toast.makeText(context,". قم بتسجيل الدخول مره اخرى", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    val myintint = Intent(context, LoginSiginpActivity::class.java)
                    myintint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(myintint)
//                    callback(null)
                }
            }

        }
        else{
            callback(null)
        }
    }


    fun chickAdmin(context: Context,callback: (Boolean) -> Unit){
        chickUser(context){userDate ->
            if (userDate != null) {

                var userId = UserServes().getUserID()

                if (userId == "9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || userId =="VfEvVuiJTdbCep7fQcYQVG6MivH3"){
                    callback(true)
                }
                else{
                    callback(false)
                }
            }
            else{
                callback(false)
            }

        }
    }

}