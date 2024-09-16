package com.smartherd.alameer3.activitys.activty


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.models.Tess
import java.util.concurrent.TimeUnit


class PhoneLoginActivity : AppCompatActivity() {
    private lateinit var btn_sea: Button
    private lateinit var sizemm: EditText
    private lateinit var prices: EditText
    private lateinit var btn_ssend: Button
    private lateinit var prog_phone: ProgressBar
    private lateinit var ss_test:Spinner
    var storedVerificationId = ""
    private lateinit var card_mostafa_test:CardView
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken


   var  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("sendPhone", "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }



        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("sendPhone", "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.d("sendPhone", e.message.toString())
            } else if (e is FirebaseTooManyRequestsException) {
                Log.d("sendPhone", e.message.toString())
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                Log.d("sendPhone", e.message.toString())
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("sendPhone", "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_login)

        imelmnt()





        var auth = FirebaseAuth.getInstance()

        if (auth.currentUser!!.uid=="9cuzF1pt6DRAduNUd6ZGqWrGxeC2"){
            card_mostafa_test.visibility = View.VISIBLE
        }
//        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true)





        FirebaseMessaging.getInstance().subscribeToTopic("testadmin").addOnSuccessListener { ss2 ->

            Toast.makeText(this,"done",Toast.LENGTH_SHORT).show()
        }


        btn_ssend.setOnClickListener {

            var phone = sizemm.text.toString()

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("sendPhone", "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("sendPhone", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    private fun imelmnt() {
        sizemm = findViewById(R.id.sizemm)
        prices = findViewById(R.id.prices)
        btn_sea = findViewById(R.id.btn_sea)
        btn_ssend = findViewById(R.id.btn_ssend)
        prog_phone = findViewById(R.id.prog_phone)
        ss_test = findViewById(R.id.ss_test)
        card_mostafa_test = findViewById(R.id.card_mostafa_test)
    }

    fun pricess(){
        prices.visibility = View.VISIBLE
    }

}

