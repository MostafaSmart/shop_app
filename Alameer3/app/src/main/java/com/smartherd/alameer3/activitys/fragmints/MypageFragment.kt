package com.smartherd.alameer3.activitys.fragmints

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.Helpers.FragmentChangeListener
import com.smartherd.alameer3.activitys.activty.AboutActivity
import com.smartherd.alameer3.activitys.activty.LoginSiginpActivity
import com.smartherd.alameer3.activitys.activty.ProfileActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MypageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MypageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var fragmentChangeListener: FragmentChangeListener? = null
    private lateinit var opt_1: LinearLayout
    private lateinit var opt_2: LinearLayout
    private lateinit var opt_3: LinearLayout
    private lateinit var opt_4: LinearLayout
    private lateinit var brn_whatsapp: Button
    private lateinit var btn_facebook: Button
    private lateinit var btn_telegram: Button
    private lateinit var btn_login_prf: Button
    private lateinit var btn_instagram: Button
    private lateinit var pr_email_text: TextView
    private lateinit var pr_username_text: TextView
    private lateinit var acount_profile: LinearLayout
    private lateinit var show_cosr_orders: LinearLayout
    private lateinit var contorys_citys:LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            fragmentChangeListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_mypage, container, false)


        implmnt(view)
        chick()

        onClickLisins(view)





        return view
    }

    private fun onClickLisins(view: View) {

        btn_login_prf.setOnClickListener {
            val intent = Intent(view.context, LoginSiginpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        }


        acount_profile.setOnClickListener {
            val intent = Intent(view.context, ProfileActivity::class.java)
            startActivity(intent)

        }
        contorys_citys.setOnClickListener {
            fragmentChangeListener?.onFragmentChange(AddContryFragment())
        }
        show_cosr_orders.setOnClickListener {
            fragmentChangeListener?.onFragmentChange(AdmingetOrdersFragment())

        }

        btn_instagram.setOnClickListener {
            Toast.makeText(view.context,"يرجى الانتظار ",Toast.LENGTH_SHORT).show()
            var firestore = FirebaseFirestore.getInstance().collection("admin").document("info")
            firestore.get().addOnSuccessListener {
                if(it.get("insta").toString().isNotEmpty()){
                    goToInsta(it.get("insta").toString())
                }else{
                    Toast.makeText(view.context,"غير متوفر حاليا ",Toast.LENGTH_SHORT).show()

                }
            }
        }
        brn_whatsapp.setOnClickListener {
            Toast.makeText(view.context,"يرجى الانتظار ",Toast.LENGTH_SHORT).show()
            var firestore = FirebaseFirestore.getInstance().collection("admin").document("info")
            firestore.get().addOnSuccessListener {
                if(it.get("whatsapp").toString().isNotEmpty()){
                    goWhatsapp(view,it.get("whatsapp").toString())
                }else{
                    Toast.makeText(view.context,"غير متوفر حاليا ",Toast.LENGTH_SHORT).show()

                }
            }


        }

        btn_telegram.setOnClickListener {
            Toast.makeText(view.context,"يرجى الانتظار ",Toast.LENGTH_SHORT).show()
           var firestore = FirebaseFirestore.getInstance().collection("admin").document("info")
            firestore.get().addOnSuccessListener {
                if(it.get("phone").toString().isNotEmpty()){
                    dialPhoneNumber(it.get("phone").toString())
                }else{
                    Toast.makeText(view.context,"غير متوفر حاليا ",Toast.LENGTH_SHORT).show()

                }
            }
        }


        btn_facebook.setOnClickListener {
            Toast.makeText(view.context,"يرجى الانتظار ",Toast.LENGTH_SHORT).show()
            var firestore = FirebaseFirestore.getInstance().collection("admin").document("info")
            firestore.get().addOnSuccessListener {
                if(it.get("facebook").toString().isNotEmpty()){
                    goToFacebook(it.get("facebook").toString())
                }else{
                    Toast.makeText(view.context,"غير متوفر حاليا ",Toast.LENGTH_SHORT).show()

                }
            }
        }


        opt_1.setOnClickListener {
            var intent = Intent(view.context,AboutActivity::class.java)
            intent.putExtra("aboutFlag","about_store")
            startActivity(intent)
        }

        opt_3.setOnClickListener {
            var intent = Intent(view.context,AboutActivity::class.java)
            intent.putExtra("aboutFlag","about_div")
            startActivity(intent)
        }
        opt_2.setOnClickListener {
            var intent = Intent(view.context,AboutActivity::class.java)
            intent.putExtra("aboutFlag","priv")
            startActivity(intent)
        }

    }

    private fun chick(){
        if(FirebaseAuth.getInstance().currentUser!=null){
            acount_profile.visibility = View.VISIBLE
            btn_login_prf.visibility = View.GONE
            show_cosr_orders.visibility = View.VISIBLE
            if(FirebaseAuth.getInstance().currentUser!!.uid=="9cuzF1pt6DRAduNUd6ZGqWrGxeC2"
                || FirebaseAuth.getInstance().currentUser!!.uid=="VsEoPCyXGGdIqnrVa1S1hp2b4w82"
                || FirebaseAuth.getInstance().currentUser!!.uid=="VfEvVuiJTdbCep7fQcYQVG6MivH3"){
                contorys_citys.visibility = View.VISIBLE

            }

            var firestore = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            firestore.get().addOnSuccessListener {
                pr_username_text.text = it.get("name").toString()
                pr_email_text.text = it.get("email").toString()
            }

        }
    }

    fun goToInsta(id:String){
        var url = "https://www.instagram.com/"+id+"/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(Intent.createChooser(intent, "اختر تطبيق للفتح"))
    }

    fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun goWhatsapp(view: View,phoneNumber:String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phoneNumber"))

        // الحصول على قائمة بجميع التطبيقات المثبتة التي يمكن استخدامها لفتح النية
        val packageManager = view.context.packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        val isIntentSafe = activities.isNotEmpty()

        if (isIntentSafe) {
            // عرض خيارات للمستخدم لتحديد التطبيق الذي يناسبه لفتح النية
            val chooser = Intent.createChooser(intent, "Open with")
            startActivity(chooser)
        } else {
            // في حالة عدم توفر تطبيقات WhatsApp على جهاز المستخدم، يتم عرض رسالة خطأ للمستخدم
            Toast.makeText(
                view.context,
                "WhatsApp is not installed on your device",
                Toast.LENGTH_SHORT
            ).show()
        }
    }



    fun goToFacebook(id: String){
        var url = "https://www.facebook.com/"+id
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(Intent.createChooser(intent, "اختر تطبيق للفتح"))

    }


    private fun implmnt(view: View) {
        opt_1 = view.findViewById(R.id.opt_1)
        opt_2 = view.findViewById(R.id.opt_2)
        opt_3 = view.findViewById(R.id.opt_3)
        opt_4 = view.findViewById(R.id.opt_4)
        brn_whatsapp = view.findViewById(R.id.brn_whatsapp)
        btn_facebook = view.findViewById(R.id.btn_facebook)
        btn_telegram = view.findViewById(R.id.btn_telegram)
        pr_email_text = view.findViewById(R.id.pr_email_text)
        btn_login_prf = view.findViewById(R.id.btn_login_prf)
        btn_instagram = view.findViewById(R.id.btn_instagram)
        acount_profile = view.findViewById(R.id.acount_profile)
        pr_username_text = view.findViewById(R.id.pr_username_text)
        show_cosr_orders = view.findViewById(R.id.show_cosr_orders)
        contorys_citys = view.findViewById(R.id.contorys_citys)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MypageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MypageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}