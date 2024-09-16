package com.smartherd.alameer3.activitys.fragmints

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.MainActivity
import com.smartherd.alameer3.activitys.activty.AddprodactActivity
import com.smartherd.alameer3.activitys.activty.LoginSiginpActivity
import com.smartherd.alameer3.activitys.activty.PhoneLoginActivity
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
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
const val request_code=2
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var btn_login: Button
    private lateinit var btn_go_without: Button
    private lateinit var prog_login: ProgressBar
    private lateinit var btn_login_google: Button
    private lateinit var login_email_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var login_password_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var dialog:Dialog
    private lateinit var btn_finsh_Sghn: Button
    private lateinit var coplet_name_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var coplet_phone_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var loction_input:Spinner
    private lateinit var text_rest_password:TextView

    var flag = false
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

        val view = inflater.inflate(R.layout.fragment_login, container, false)


        imilmnt(view)




        btn_login_google.setOnClickListener {

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
        btn_finsh_Sghn.setOnClickListener {

            if (coplet_name_input.text.toString().isNotEmpty() && coplet_phone_input.text.toString().isNotEmpty() && loction_input.selectedItem.toString().isNotEmpty())
            {

                firestore = FirebaseFirestore.getInstance()
                var name = coplet_name_input.text.toString()
                var email = firebaseAuth.currentUser!!.email.toString()
                var phone = coplet_phone_input.text.toString()
                var loc = loction_input.selectedItem.toString()



                if (coplet_name_input.text.toString().length<7){
                    coplet_name_input.error = "يجب ادخال الاسم كاملاً"
                    coplet_name_input.requestFocus()
                    return@setOnClickListener
                }

                if (coplet_phone_input.text.toString().length!=9){
                    coplet_phone_input.error = "يجب ادخال رقم هاتف صحيح"
                    coplet_phone_input.requestFocus()
                    return@setOnClickListener
                }




                dialog.cancel()

                FirebaseMessaging.getInstance().token.addOnSuccessListener { taskToken ->
                    var user1 = Users(name, email, phone,loc,taskToken.toString(),"0")

                    firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                        .set(user1).addOnSuccessListener {
                            prog_login.visibility = View.GONE
                            Toast.makeText(requireActivity(), "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show()
                            flag = true
                            reload()

                        }


                }





            }
            else{
                coplet_name_input.error ="يرجى ادخال جميع البيانات المطلوبة"
                coplet_phone_input.error ="يرجى ادخال جميع البيانات المطلوبة"
                flag = false

            }
        }



        btn_login.setOnClickListener {

//            val muintin = Intent(requireActivity(), PhoneLoginActivity::class.java)
//            startActivity(muintin)
            doLogin()
        }

        btn_go_without.setOnClickListener {
            reload()
        }

        val Loction = ArrayAdapter(requireActivity(), R.layout.spinner_item, countries)
        loction_input.adapter = Loction


        text_rest_password.setOnClickListener {
            prog_login.visibility = View.VISIBLE
            Toast.makeText(view.context,"يرجى الانتظار..",Toast.LENGTH_SHORT).show()
            if (login_email_input.text.toString().isNotEmpty()){
                var em =login_email_input.text.toString()

                FirebaseAuth.getInstance().sendPasswordResetEmail(em).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        prog_login.visibility = View.GONE
                        Toast.makeText(view.context,"تم ارسال كود الاستعادة الى عنوان بريدك الالكتروني.",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(view.context,"حدث خطاء يرجى التأكد من صحة البيانات",Toast.LENGTH_SHORT).show()
                        prog_login.visibility = View.GONE

                    }
                }

            }
            else{

                Toast.makeText(view.context,"اكتب عنوان بريدك اولاً . ثم اضغط مره اخرى",Toast.LENGTH_LONG).show()
                prog_login.visibility = View.GONE

            }

        }

        return view
    }



    private fun doLogin() {
        if (login_email_input.text.toString().isNotEmpty() && login_password_input.text.toString()
                .isNotEmpty()
        ) {
            val email = login_email_input.text.toString()
            val password = login_password_input.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                login_email_input.error = "صيغة البريد الالكتروني غير صحيحة"
                login_email_input.requestFocus()
                return
            }
            if (password.length < 6) {
                login_password_input.error = "كلمة المرور خاطئة"
                login_password_input.requestFocus()
                return
            }
            prog_login.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {

                FirebaseMessaging.getInstance().token.addOnSuccessListener {taskToken->
                    if(taskToken.toString().isNotEmpty()){
                        var token = taskToken.toString()

                        if(firebaseAuth.currentUser!!.uid!="9cuzF1pt6DRAduNUd6ZGqWrGxeC2"){
                            FirebaseMessaging.getInstance().subscribeToTopic("sendAll").addOnSuccessListener { ss2 ->
                                var fire1 = FirebaseFirestore.getInstance().collection("users")
                                    .document(firebaseAuth.currentUser!!.uid)
                                fire1.get().addOnSuccessListener { uu ->
                                    if (uu.exists()) {

                                    var user = uu.toObject(Users::class.java)!!
                                    user.Token = token
                                    fire1.set(user).addOnSuccessListener {
                                        prog_login.visibility = View.GONE
                                        if (firebaseAuth.currentUser!!.isEmailVerified) {
                                            reload()

                                        } else {
                                            val muint = Intent(
                                                requireActivity(),
                                                VerificationActivity::class.java
                                            )
                                            muint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                            startActivity(muint)
                                        }
                                    }


                                }
                                    else{
                                        Toast.makeText(requireActivity(),". قم بتسجيل الدخول مره اخرى", Toast.LENGTH_SHORT).show()
                                        FirebaseAuth.getInstance().signOut()
                                        val myintint = Intent(requireActivity(), LoginSiginpActivity::class.java)
                                        myintint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                        startActivity(myintint)
                                        Toast.makeText(requireActivity(),"اذا استمرت المشكلة ..تواصل معنا", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            }

                        }
                        else{
                            var fire1= FirebaseFirestore.getInstance().collection("users").document(firebaseAuth.currentUser!!.uid)
                            fire1.get().addOnSuccessListener { uu->
                                var user = uu.toObject(Users::class.java)!!
                                user.Token = token
                                fire1.set(user).addOnSuccessListener {
                                    prog_login.visibility = View.GONE
                                    reload()
                                }


                            }

                        }



                    }

                }


            }.addOnFailureListener {
                Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_SHORT).show()
                prog_login.visibility = View.GONE
            }


        }
    }

    private fun reload() {
        val muintin = Intent(requireActivity(), MainActivity::class.java)
        muintin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(muintin)
    }




    private fun imilmnt(view: View) {
        btn_login = view.findViewById(R.id.btn_login)
        prog_login = view.findViewById(R.id.prog_login)
        btn_go_without = view.findViewById(R.id.btn_go_without)
        btn_login_google = view.findViewById(R.id.btn_login_google)
        login_email_input = view.findViewById(R.id.login_email_input)
        login_password_input = view.findViewById(R.id.login_password_input)
        firebaseAuth = FirebaseAuth.getInstance()

        text_rest_password = view.findViewById(R.id.text_rest_password)



            dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.complite_the_sgin)

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)

        btn_finsh_Sghn = dialog.findViewById(R.id.btn_finsh_Sghn)
        coplet_name_input = dialog.findViewById(R.id.coplet_name_input)
        coplet_phone_input = dialog.findViewById(R.id.coplet_phone_input)
        loction_input = dialog.findViewById(R.id.loction_input)







    }

    private fun isConnected(): Boolean {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun googleToFirebace(acount:GoogleSignInAccount){
        firestore = FirebaseFirestore.getInstance()
        prog_login.visibility = View.VISIBLE

        val craid = GoogleAuthProvider.getCredential(acount.idToken,null)
        CoroutineScope(Dispatchers.IO).launch {
            try
            {
                firebaseAuth.signInWithCredential(craid).await()
                withContext(Dispatchers.Main){



                    var dp = firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
                    dp.get().addOnSuccessListener {sss->
                        if(sss.exists()){
                            FirebaseMessaging.getInstance().token.addOnSuccessListener { taskToken ->
                                if(firebaseAuth.currentUser!!.uid!="9cuzF1pt6DRAduNUd6ZGqWrGxeC2"){
                                    FirebaseMessaging.getInstance().subscribeToTopic("sendAll").addOnSuccessListener { ss2 ->
                                        val user = sss.toObject(Users::class.java)!!
                                        user.Token = taskToken.toString()
                                        dp.set(user).addOnSuccessListener {
                                            reload()


                                    }

                                    }

                                }
                                else{
                                    val user = sss.toObject(Users::class.java)!!
                                    user.Token = taskToken.toString()
                                    dp.set(user).addOnSuccessListener {
                                        reload()


                                    }
                                }

                            }

                        }
                        else{
                            dialog.show()

                        }
                    }
                }


            }
            catch (e:Exception)
            {
                withContext(Dispatchers.Main){
                    Toast.makeText(requireActivity(),e.message,Toast.LENGTH_SHORT).show()
                }

            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== request_code){
            if(data !=null){
                try {
                    val acount = GoogleSignIn.getSignedInAccountFromIntent(data).result
                    googleToFirebace(acount)

                }catch (ee:Exception){

                }

            }


        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}