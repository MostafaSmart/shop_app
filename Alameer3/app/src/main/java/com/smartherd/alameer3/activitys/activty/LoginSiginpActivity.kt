package com.smartherd.alameer3.activitys.activty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.Helpers.ViewPigerAdapter
import com.smartherd.alameer3.activitys.MainActivity
import com.smartherd.alameer3.activitys.fragmints.LoginFragment
import com.smartherd.alameer3.activitys.fragmints.SignupFragment

class LoginSiginpActivity : AppCompatActivity() {
    private lateinit var viw_piger: androidx.viewpager.widget.ViewPager
    private lateinit var tap_layout: com.google.android.material.tabs.TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_siginp)


        imelmnt()
        chek()

        var viewpigeradapter = ViewPigerAdapter(supportFragmentManager)
        viewpigeradapter.addFragment(LoginFragment(),"تسجيل الدخول")
        viewpigeradapter.addFragment(SignupFragment(),"تسجيل حساب جديد")
        viw_piger.adapter = viewpigeradapter
        tap_layout.setupWithViewPager(viw_piger)


    }

    private fun imelmnt() {
        viw_piger = findViewById(R.id.viw_piger)
        tap_layout = findViewById(R.id.tap_layout)
    }
    private fun chek() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({


            if (FirebaseAuth.getInstance().currentUser != null) {
                if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }



            }
        }, 0)
    }
}