package com.smartherd.alameer3.activitys.fragmints

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.MainActivity
import com.smartherd.alameer3.activitys.activty.AddprodactActivity
import com.smartherd.alameer3.activitys.activty.VerificationActivity
import com.smartherd.alameer3.activitys.models.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var btn_sginUp: Button
    private lateinit var prog_sginup: ProgressBar
    private lateinit var btn_sginUp_google: Button
    private lateinit var sign_name_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var sgin_email_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var sgin_phone_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var sgin_password_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var dialog: Dialog
    private lateinit var btn_finsh_Sghn: Button
    private lateinit var loction_input: Spinner
    private lateinit var loction_input_sigin: Spinner
    private var state:String?=null
    private lateinit var coplet_name_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var coplet_phone_input: com.google.android.material.textfield.TextInputEditText
    private val countries = listOf("شمال", "جنوب")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.fragment_signup, container, false)

        imlimnt(view)


        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://www.example.com/finishSignUp?cartId=1234"
            // This must be true
            handleCodeInApp = true
            setIOSBundleId("com.example.ios")
            setAndroidPackageName(
                "com.example.android",
                true, // installIfNotAvailable
                "12", // minimumVersion
            )
        }


        btn_sginUp_google.setOnClickListener {
            if(isConnected()){

                val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.clint_id))
                    .requestEmail()
                    .build()
                val sghinClain = GoogleSignIn.getClient(requireActivity(),option)
                sghinClain.signInIntent.also {
                    startActivityForResult(it, request_code)
                }
            }else{
                Toast.makeText(requireActivity(),"يرجى التحقق من اتصال الانترنت !" ,Toast.LENGTH_SHORT).show()
            }

        }
        btn_sginUp.setOnClickListener {
            finshSginUp()
        }


        btn_finsh_Sghn.setOnClickListener {

            dialogFinshSghinUp()
        }

        val Loction = ArrayAdapter(requireActivity(), R.layout.spinner_item, countries)
        loction_input.adapter = Loction
        loction_input_sigin.adapter = Loction


        return view
    }


    private fun dialogFinshSghinUp() {
        if (coplet_name_input.text.toString().isNotEmpty() && coplet_phone_input.text.toString()
                .isNotEmpty() && loction_input.selectedItem.toString().isNotEmpty()
        ) {
            state = loction_input.selectedItem.toString()
            var name = coplet_name_input.text.toString()
            var email = firebaseAuth.currentUser!!.email.toString()
            var phone = coplet_phone_input.text.toString()


            dialog.cancel()


            var muint = Intent(requireActivity(), MainActivity::class.java)



            FirebaseMessaging.getInstance().token.addOnSuccessListener { taskToken ->
                var user1 = Users(name, email, phone, state!!,taskToken,"0")
                FirebaseMessaging.getInstance().subscribeToTopic("sendAll").addOnSuccessListener { ss2 ->

                    firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                        .set(user1).addOnSuccessListener {
                            muint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            prog_sginup.visibility = View.GONE
                            Toast.makeText(requireActivity(), "secsses", Toast.LENGTH_SHORT).show()
                            startActivity(muint)
                        }

                }

            }




        } else {
            coplet_name_input.error = "يرجى ادخال جميع البيانات المطلوبة"
            coplet_phone_input.error = "يرجى ادخال جميع البيانات المطلوبة"

        }
    }


    private fun finshSginUp() {
        if (sign_name_input.text.toString().isNotEmpty() && sgin_email_input.text.toString()
                .isNotEmpty()
            && sgin_password_input.text.toString().isNotEmpty() && sgin_phone_input.text.toString()
                .isNotEmpty()
            && loction_input_sigin.selectedItem.toString().isNotEmpty()
        ) {

            var str = loction_input_sigin.selectedItem.toString()
            val name = sign_name_input.text.toString()
            val email = sgin_email_input.text.toString().trim()
            val passwoed = sgin_password_input.text.toString().trim()
            val phoneNumber = sgin_phone_input.text.toString().trim()
            prog_sginup.visibility = View.VISIBLE
            firestore = FirebaseFirestore.getInstance()



            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                sgin_email_input.error = "صيغة البريد الالكتروني غير صحيحة"
                sgin_email_input.requestFocus()
                return
            }
            if (passwoed.length < 6) {
                sgin_password_input.error = "يجب ان تحتوي على اكثر من 6 عناصر"
                sgin_password_input.requestFocus()
                return
            }

            if (sign_name_input.text.toString().length<7){
                sign_name_input.error = "يرجى ادخال الاسم الكامل"
                sign_name_input.requestFocus()
                return
            }

            if (sgin_phone_input.text.toString().length!=9){
                sgin_phone_input.error = "يرجى ادخال رقم هاتف صحيح"
                sgin_phone_input.requestFocus()
                return
            }





                try {
                    firebaseAuth.createUserWithEmailAndPassword(email, passwoed).addOnSuccessListener {task->

                        FirebaseMessaging.getInstance().token.addOnSuccessListener { taskToken ->
                            FirebaseMessaging.getInstance().subscribeToTopic("sendAll").addOnSuccessListener { ss2 ->

                                var user1 = Users(name, email, phoneNumber, str,taskToken.toString(),"0")

                                firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                                    .set(user1).addOnSuccessListener {
                                        val muint = Intent(requireActivity(), VerificationActivity::class.java)
                                        Toast.makeText(
                                            requireActivity(),
                                            "تم التسجيل بنجاح",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        muint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        prog_sginup.visibility = View.GONE
                                        startActivity(muint)
                                    }


                            }


                        }



                    }.addOnFailureListener {ee->
                        Toast.makeText(requireActivity(),ee.message.toString(),Toast.LENGTH_LONG).show()
                        prog_sginup.visibility = View.GONE

                    }




                } catch (e: Exception) {

                        Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                        prog_sginup.visibility = View.GONE


                }



        }
    }



    fun googleToFirebace(acount: GoogleSignInAccount){
        firestore = FirebaseFirestore.getInstance()
        prog_sginup.visibility = View.VISIBLE
        var muint = Intent(requireActivity(),MainActivity::class.java)
        val craid = GoogleAuthProvider.getCredential(acount.idToken,null)
        CoroutineScope(Dispatchers.IO).launch {
            try
            {

                firebaseAuth.signInWithCredential(craid).await()
                withContext(Dispatchers.Main){

                    var dp= firestore.collection("users").document(firebaseAuth.currentUser!!.uid)

                    dp.get().addOnSuccessListener { docmen->
                        if(docmen.exists()){
                            muint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            prog_sginup.visibility = View.GONE
                            Toast.makeText(requireActivity(),"secsses", Toast.LENGTH_SHORT).show()
                            startActivity(muint)

                        }
                        else
                        {
                            dialog.show()
                        }
                    }


                }


            }
            catch (e:Exception)
            {
                withContext(Dispatchers.Main){
                    Toast.makeText(requireActivity(),e.message, Toast.LENGTH_SHORT).show()
                    prog_sginup.visibility = View.GONE
                }

            }

        }
    }


    private fun isConnected(): Boolean {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== request_code){
            if(data !=null) {
                try {
                    val acount = GoogleSignIn.getSignedInAccountFromIntent(data).result
                    googleToFirebace(acount)
                } catch (ee: Exception) {

                }
            }

        }
    }


    private fun imlimnt(view: View) {
        btn_sginUp = view.findViewById(R.id.btn_sginUp)
        prog_sginup = view.findViewById(R.id.prog_sginup)
        sign_name_input = view.findViewById(R.id.sign_name_input)
        sgin_email_input = view.findViewById(R.id.sgin_email_input)
        sgin_phone_input = view.findViewById(R.id.sgin_phone_input)
        btn_sginUp_google = view.findViewById(R.id.btn_sginUp_google)
        sgin_password_input = view.findViewById(R.id.sgin_password_input)
        firebaseAuth = FirebaseAuth.getInstance()
        loction_input_sigin = view.findViewById(R.id.loction_input_sigin)

        dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.complite_the_sgin)


        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)


        btn_finsh_Sghn = dialog.findViewById(R.id.btn_finsh_Sghn)
        coplet_name_input = dialog.findViewById(R.id.coplet_name_input)
        coplet_phone_input =dialog.findViewById(R.id.coplet_phone_input)
        loction_input = dialog.findViewById(R.id.loction_input)







    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}