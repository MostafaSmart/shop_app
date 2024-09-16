package com.smartherd.alameer3.activitys.activty

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.MainActivity

class SplechActivity : AppCompatActivity() {



    private lateinit var imageView: ImageView

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor




    override fun onCreate(savedInstanceState: Bundle?) {







        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_splech)
        splashScreen.setKeepOnScreenCondition { true }

        impelmnt()

//        val animation2 = TranslateAnimation(200f, 0f, 0f, 0f)
//        val animation = TranslateAnimation(-200f, 0f, 0f, 0f)
//        animation.duration = 1000
//        animation.fillAfter = true
//
//        animation2.duration =1000
//        animation2.fillAfter =true
//
//        imageView.startAnimation(animation2)




        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            if(FirebaseAuth.getInstance().currentUser!=null)
            {

                if (FirebaseAuth.getInstance().currentUser!!.email == "naillsmart112233@gmail.com" || FirebaseAuth.getInstance().currentUser!!.email =="ar777732788@gmail.com"){
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {

                        val intent = Intent(this,LoginSiginpActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }




            }
            else
            {

                val intent = Intent(this,LoginSiginpActivity::class.java)
                startActivity(intent)
                finish()
            }


        },3000)



    }

    private fun impelmnt() {
        imageView = findViewById(R.id.imageView)
    }
}