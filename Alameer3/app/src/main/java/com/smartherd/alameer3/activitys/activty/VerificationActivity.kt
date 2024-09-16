package com.smartherd.alameer3.activitys.activty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.MainActivity

class VerificationActivity : AppCompatActivity() {


    private lateinit var prog_send_code: ProgressBar
    private lateinit var btn_send_emeil_code: android.widget.Button
    private lateinit var btn_send_emeil_close: android.widget.Button
    private lateinit var show_email_vald: com.google.android.material.textfield.TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)






        imelmnt()

        var auth = FirebaseAuth.getInstance()
        if (auth.currentUser!=null){
            var email =  auth.currentUser!!.email
            show_email_vald.setText(email)

        }
        else{
            Toast.makeText(this,"حصل خطاء . اعد المحاولة مرة اخرى ",Toast.LENGTH_SHORT).show()
            finish()
        }

        btn_send_emeil_code.setOnClickListener {
            prog_send_code.visibility= View.VISIBLE
            auth.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    prog_send_code.visibility= View.GONE

                    Log.d("email", "Email sent.")
                    val snackbar = Snackbar.make(
                        show_email_vald,
                        "تم ارسال رمز التحقق الى البريد الالكتروني ",
                        Snackbar.LENGTH_LONG
                    )

                    snackbar.show()



                    val timerDuration = 60000
                    var counter = 60
                    btn_send_emeil_code.isClickable = false


                    val timer = object : CountDownTimer(timerDuration.toLong(), 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            counter--
                            btn_send_emeil_code.text = counter.toString()
                        }

                        override fun onFinish() {
                            prog_send_code.progress = 100
                            btn_send_emeil_code.isClickable = true
                            btn_send_emeil_code.text = "اعادة الارسال"
                        }
                    }
                    timer.start()
                }

            }
        }


        btn_send_emeil_close.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginSiginpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun imelmnt() {



        prog_send_code = findViewById(R.id.prog_send_code)
        show_email_vald = findViewById(R.id.show_email_vald)
        btn_send_emeil_code = findViewById(R.id.btn_send_emeil_code)
        btn_send_emeil_close = findViewById(R.id.btn_send_emeil_close)


    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()

    }
}