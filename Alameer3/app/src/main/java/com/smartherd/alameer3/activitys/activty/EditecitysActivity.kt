package com.smartherd.alameer3.activitys.activty

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.adapders.CitysAdapter
import com.smartherd.alameer3.activitys.models.Citys
import java.util.ArrayList

class EditecitysActivity : AppCompatActivity() {
    private lateinit var citys_back: ImageView
    private lateinit var citys_tital: TextView
    private lateinit var brn_add_ciyts: FloatingActionButton
    private lateinit var linearLayout3: LinearLayout
    private lateinit var citys_list: androidx.recyclerview.widget.RecyclerView

    private lateinit var dialog: Dialog
    private lateinit var btn_save_cont: Button
    private lateinit var input_contory: EditText
    private lateinit var contory_price: EditText
    private lateinit var prog_add_contory: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editecitys)


        imelmnt()

        var contory = intent.getStringExtra("contory")

        getCityesByContory(contory)
        brn_add_ciyts.setOnClickListener {
            contory_price.visibility = View.GONE
            dialog.show()
        }



    }



    private fun getCityesByContory(contory: String?) {
        citys_list.layoutManager = LinearLayoutManager(this)


        var firestore = FirebaseFirestore.getInstance().collection("city").document("data")
        firestore.get().addOnSuccessListener {
            var dataCity = it.toObject(Citys::class.java)!!

            var cityis = dataCity.cit.get(contory)

            var adapter1 = CitysAdapter(cityis!!)
            citys_list.adapter = adapter1

            addCityToContory(cityis, dataCity, contory, firestore)




        }
    }

    private fun addCityToContory(cityis: ArrayList<String>, dataCity: Citys, contory: String?, firestore: DocumentReference)
    {
        btn_save_cont.setOnClickListener {
            if (input_contory.text.toString().isNotEmpty()) {
                prog_add_contory.visibility = View.VISIBLE
                val newCity = input_contory.text.toString()
                cityis.add(newCity)

                var newData = dataCity
                newData.cit.put(contory!!, cityis)

                firestore.set(newData).addOnSuccessListener {
                    prog_add_contory.visibility = View.GONE
                    Toast.makeText(this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }

            }
        }
    }

    private fun imelmnt() {

        citys_back = findViewById(R.id.citys_back)
        citys_list = findViewById(R.id.citys_list)
        citys_tital = findViewById(R.id.citys_tital)
        linearLayout3 = findViewById(R.id.linearLayout3)
        brn_add_ciyts = findViewById(R.id.brn_add_ciyts)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_contory)


        input_contory = dialog.findViewById(R.id.input_contory)
        contory_price = dialog.findViewById(R.id.contory_price)
        btn_save_cont = dialog.findViewById(R.id.btn_save_cont)
        prog_add_contory = dialog.findViewById(R.id.prog_add_contory)



        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        dialog.window?.setLayout((width * 1).toInt(), (height * 0.8).toInt())
    }
}