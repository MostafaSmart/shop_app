package com.smartherd.alameer3.activitys.activty

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.R

class AboutActivity : AppCompatActivity() {
    private lateinit var about_body: LinearLayout
    private lateinit var heder_about: LinearLayout
    private lateinit var ab_div: LinearLayout
    private lateinit var ab_store: LinearLayout

    private lateinit var priv: LinearLayout

    private lateinit var heder_bake: ImageView
    private lateinit var hedar_tital: TextView

    private lateinit var contact_whtaspp_div: ImageView
    private lateinit var about_contener_div: LinearLayout
    private lateinit var contact_instagram_div: ImageView

    private lateinit var go_googleMaps: Button
    private lateinit var contenr_store: ScrollView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)







        imelmnt()

        var flag = intent.getStringExtra("aboutFlag")
        heder_bake.setOnClickListener {
            finish()
        }
        checkFlagIncom(flag)




    }

    private fun checkFlagIncom(flag: String?) {


        if (flag != null) {
            when (flag) {
                "about_div" -> {
                    aboutDivLyout()

                }
                "about_store" -> {
                    aboutStoreLayout()
                }
                "priv" ->{
                    privacyPolicy()
                }


            }
        }
    }

    private fun aboutStoreLayout() {

        ab_store.visibility = View.VISIBLE
        hedar_tital.text = "عن المتجر"

        contenr_store = ab_store.findViewById(R.id.contenr_store)
        go_googleMaps = ab_store.findViewById(R.id.go_googleMaps)



        go_googleMaps.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.app.goo.gl/GMNs1Snm6n89WRe46"))
            startActivity(Intent.createChooser(intent, "اختر تطبيق للفتح"))
        }
    }
    private fun privacyPolicy() {

        priv.visibility = View.VISIBLE
        hedar_tital.text = "ساسية الخصوصية"





    }

    private fun aboutDivLyout() {

        hedar_tital.text ="عن المطور"

        ab_div.visibility = View.VISIBLE

        about_contener_div = ab_div.findViewById(R.id.about_contener_div)
        contact_whtaspp_div = ab_div.findViewById(R.id.contact_whtaspp_div)
        contact_instagram_div = ab_div.findViewById(R.id.contact_instagram_div)

        contact_whtaspp_div.setOnClickListener {
            Toast.makeText(this,"يرجى الانتظار ",Toast.LENGTH_SHORT).show()
            var firestore = FirebaseFirestore.getInstance().collection("admin").document("dev")
            firestore.get().addOnSuccessListener {
                if(it.get("phone").toString().isNotEmpty()){
                    dialPhoneNumber(it.get("phone").toString())
                }else{
                    Toast.makeText(this,"غير متوفر حاليا ",Toast.LENGTH_SHORT).show()

                }
            }

        }
        contact_instagram_div.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/65sw_/"))
            startActivity(Intent.createChooser(intent, "اختر تطبيق للفتح"))
        }
    }

    fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }




    private fun imelmnt() {
        about_body = findViewById(R.id.about_body)
        heder_about = findViewById(R.id.heder_about)
        ab_div = findViewById(R.id.ab_div)
        ab_store = findViewById(R.id.ab_store)
        priv  = findViewById(R.id.priv)

        heder_bake = heder_about.findViewById(R.id.heder_bake)
        hedar_tital = heder_about.findViewById(R.id.hedar_tital)


    }
}