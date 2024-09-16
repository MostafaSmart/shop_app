package com.smartherd.alameer3.activitys.fragmints

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.MainActivity
import com.smartherd.alameer3.activitys.activty.ComliteorderActivity
import com.smartherd.alameer3.activitys.adapders.SalahAdapter
import com.smartherd.alameer3.activitys.models.Clat
import com.smartherd.alameer3.activitys.models.ClatProdact
import com.smartherd.alameer3.activitys.models.Prodacts
import com.smartherd.alameer3.activitys.serves.GetSalah
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SalahFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SalahFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var btn_conton_bay: Button
    private lateinit var prog_salah: ProgressBar
    private lateinit var salah_sgin: LinearLayout
    private lateinit var btn_salah_go_sgin: Button
    private lateinit var totl_salah_cost: TextView
    private lateinit var salah_conter: LinearLayout
    private lateinit var salah_loding: com.facebook.shimmer.ShimmerFrameLayout
    private lateinit var list_salah: androidx.recyclerview.widget.RecyclerView

    private lateinit var btn_refrish:Button

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
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_salah, container, false)


        imelmnt(view)
        chick(view){

        }


        btn_refrish.setOnClickListener {
            salah_loding.visibility = View.VISIBLE
            salah_loding.startShimmer()
            Toast.makeText(view.context,"جاري تحديث السلة",Toast.LENGTH_SHORT).show()
            chick(view){
                Toast.makeText(view.context,"تم تحديث السلة",Toast.LENGTH_SHORT).show()

            }

        }



        btn_conton_bay.setOnClickListener {

            Toast.makeText(view.context,"يرجى الانتظار",Toast.LENGTH_SHORT).show()
            chick(view){
                if(totl_salah_cost.text.toString().isNotEmpty()){

                    var intent = Intent(btn_conton_bay.context,ComliteorderActivity::class.java)
                    intent.putExtra("totalPrice",totl_salah_cost.text.toString())
                    startActivity(intent)
                }
                else{
                    Toast.makeText(view.context,"السلة مازالت فارغة !",Toast.LENGTH_SHORT).show()
                }
            }


        }





        return view
    }



    private fun chick(view: View,callback: () -> Unit){
        if(FirebaseAuth.getInstance().currentUser == null){


            salah_loding.stopShimmer()
            // تطبيق الأنيميشن للاختفاء السلس
            salah_loding.animate()
                .alpha(0f) // جعل الشفافية 0 (الاختفاء)
                .setDuration(500) // مدة الأنيميشن بالمللي ثانية
                .withEndAction {
                    // بعد انتهاء الأنيميشن، اجعل العنصر غير مرئي
                    salah_loding.visibility = View.GONE
                    salah_conter.visibility = View.VISIBLE // إظهار البيانات المطلوبة
                    salah_sgin.visibility = View.VISIBLE
                }
                .start()

        }
        else{

            totl_salah_cost.text = ""
            list_salah.adapter = null
            salah_sgin.visibility = View.GONE
            list_salah.layoutManager =  LinearLayoutManager(activity)
           GetSalah().getCaltProdactsFirst(totl_salah_cost,list_salah,salah_loding,salah_conter){
               callback()
           }



        }
    }


    private fun imelmnt(view: View) {
        salah_sgin = view.findViewById(R.id.salah_sgin)
        list_salah = view.findViewById(R.id.list_salah)
        prog_salah = view.findViewById(R.id.prog_salah)
        salah_loding = view.findViewById(R.id.salah_loding)
        salah_conter = view.findViewById(R.id.salah_conter)
        btn_conton_bay = view.findViewById(R.id.btn_conton_bay)
        totl_salah_cost = view.findViewById(R.id.totl_salah_cost)
        btn_salah_go_sgin = view.findViewById(R.id.btn_salah_go_sgin)
        btn_refrish = view.findViewById(R.id.btn_refrish)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SalahFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SalahFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}