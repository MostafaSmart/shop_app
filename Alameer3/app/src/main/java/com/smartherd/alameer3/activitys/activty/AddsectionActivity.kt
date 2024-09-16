package com.smartherd.alameer3.activitys.activty

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.models.Section
import org.w3c.dom.Text
import java.util.Calendar

class AddsectionActivity : AppCompatActivity() {
    private lateinit var prog_sec: ProgressBar
    private lateinit var btn_add_section: Button
    private lateinit var addsec_heder:LinearLayout
    private lateinit var input_new_sectin: EditText
    private lateinit var heder_bake:ImageView
    private lateinit var hedar_tital:TextView
    private lateinit var select_section:Spinner
    private lateinit var btn_delet_section:Button
    private lateinit var dialog2: Dialog
    private lateinit var prog_delete: ProgressBar
    private lateinit var btn_delete_cansel: Button
    private lateinit var btn_delete_conferm: Button
    private lateinit var delet_text:TextView


    private lateinit var sec_parent: LinearLayout
    private lateinit var select_section3: Spinner

    private lateinit var chick_reorder_section: CheckBox
    private lateinit var select_section3_name_order: Spinner
    private lateinit var select_section3_number_order: Spinner

    private lateinit var btn_save_newNamw: android.widget.Button

    private lateinit var btn_save_newOrder: android.widget.Button
    private lateinit var sec_parent2: androidx.cardview.widget.CardView
    private lateinit var btn_save_newOrder_final:Button
    private lateinit var input_newName:EditText
    private lateinit var summary_secton_order:EditText

    var finalVount =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addsection)






        imelmnt()

        hedar_tital.text = "ادارة قسم"
        btn_add_section.setOnClickListener {
            addSection()
        }
        heder_bake.setOnClickListener {
            finish()
        }
        onClickListrinr()

        GetSections()



    }

    private fun onClickListrinr(){
        chick_reorder_section.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                sec_parent2.visibility = View.VISIBLE
                summary_secton_order.visibility = View.VISIBLE
                sec_parent.visibility = View.GONE

            }
            else{
                sec_parent2.visibility = View.GONE
                sec_parent.visibility = View.VISIBLE
                summary_secton_order.visibility = View.GONE
            }
        }


    }
    private fun GetSections() {
        var firestore = FirebaseFirestore.getInstance().collection("sections")
        var section = ArrayList<String>()
        var sID = ArrayList<String>()
        var count = 0
        var map2 = HashMap<String,String>()
        var orderString = ArrayList<String>()
        var nameNewOrder = ArrayList<String>()
        firestore.orderBy("order").get().addOnSuccessListener {
            it.documents.forEach { sdoc ->
                var dd = sdoc.toObject(Section::class.java)!!
                section.add(dd.name)
                sID.add(sdoc.id)
                nameNewOrder.add(sdoc.id)
                map2.put(dd.name,dd.order)
                count++
                finalVount++
                orderString.add(count.toString())

            }
            var sectionIndex = -1
            select_section.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, section)
            select_section3_name_order.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, section)

            select_section3_number_order.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, orderString)

            select_section3.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, section)



            var sum1="الترتيب الحالي:- \n "
            map2.keys.forEach { nn->
                sum1 = sum1 + nn+" = " + map2[nn].toString()+" \n"

            }

            summary_secton_order.setText(sum1)
            select_section.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sectionIndex = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

            btn_delet_section.setOnClickListener {
                dialog2.show()
            }

            btn_delete_conferm.setOnClickListener {
                deleteSectionConform(firestore, sID, sectionIndex)

            }



            var nameIndex2 = -1



            select_section3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    nameIndex2 = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

            btn_save_newNamw.setOnClickListener {
                if(nameIndex2!=-1 && input_newName.text.toString().isNotEmpty()){
                    var newName = input_newName.text.toString()
                    prog_sec.visibility=View.VISIBLE

                    firestore.document(sID[nameIndex2]).update("name",newName).addOnSuccessListener {
                        prog_sec.visibility=View.GONE
                        Toast.makeText(this,"تم تغير الاسم بنجاح",Toast.LENGTH_SHORT).show()
                        finish()
                    }

                }
            }


            var index_name = -1
            var map1 = HashMap<String,String>()
            var map3 = HashMap<String,String>()

            select_section3_name_order.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    index_name = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }



            sum1 = sum1+ "الترتيب الجديد :- "+"\n"
            btn_save_newOrder.setOnClickListener {
                if(index_name != -1 && select_section3_number_order.selectedItem!=null){

                    map1.put(sID[index_name],select_section3_number_order.selectedItem.toString())
                    map3.put(section[index_name],select_section3_number_order.selectedItem.toString())

                    summary_secton_order.text.clear()
                    summary_secton_order.setText(sum1)
                    map3.keys.forEach {  nn->
                        summary_secton_order.setText(summary_secton_order.text.toString()+nn +" = " + map3[nn].toString()+"\n")
                    }
                    Toast.makeText(this,map1.size.toString(),Toast.LENGTH_SHORT).show()

                    if (map1.size == sID.size){
                        Toast.makeText(this,"تم ترتيب الاقسام بالكامل يمكنك الان الحفظ",Toast.LENGTH_LONG).show()
                        btn_save_newOrder_final.visibility = View.VISIBLE
                    }
                }
            }
            var finsh=0
            btn_save_newOrder_final.setOnClickListener {
                if (map1.size == sID.size){
                    prog_sec.visibility = View.VISIBLE
                    map1.keys.forEach {doc->
                        firestore.document(doc).update("order",map1[doc].toString()).addOnSuccessListener {
                            finsh++
                            if(finsh == sID.size){
                                Toast.makeText(this,"تم الحفظ بنجاح",Toast.LENGTH_SHORT).show()
                                prog_sec.visibility = View.GONE
                                finish()

                            }
                        }
                    }

                }
            }


        }
    }

    private fun deleteSectionConform(
        firestore: CollectionReference,
        sID: ArrayList<String>,
        sectionIndex: Int
    ) {
        prog_delete.visibility = View.VISIBLE
        var doc = ArrayList<String>()
        var count = 0
        firestore.document(sID[sectionIndex]).delete().addOnSuccessListener {
            var dp = FirebaseFirestore.getInstance().collection("prodacts2")
            dp.whereEqualTo("category", sID[sectionIndex]).get().addOnSuccessListener {
                it.documents.forEach { dd ->
                    doc.add(dd.id)
                }

                doc.forEach { ref ->
                    dp.document(ref).delete().addOnSuccessListener {
                        count++
                        if (doc.size == count) {
                            Toast.makeText(this, "تم الحذف بنجاح", Toast.LENGTH_SHORT)
                                .show()
                            prog_delete.visibility = View.GONE
                            dialog2.cancel()
                            finish()

                        }
                    }
                }
            }
        }
    }

    private fun addSection() {
        if (input_new_sectin.text.toString().isNotEmpty()) {

            finalVount++
            var firestore = FirebaseFirestore.getInstance()
            var sec = Section(input_new_sectin.text.toString(),Calendar.getInstance().time,finalVount.toString())
            prog_sec.visibility = View.VISIBLE
            var newr = firestore.collection("sections").document()
            newr.set(sec).addOnSuccessListener {
                Toast.makeText(this, "تمت الاضافة  بنجاح", Toast.LENGTH_SHORT).show()
                prog_sec.visibility = View.GONE

                finish()
            }

        }
    }

    private fun imelmnt() {
        prog_sec = findViewById(R.id.prog_sec)
        btn_add_section = findViewById(R.id.btn_add_section)
        input_new_sectin = findViewById(R.id.input_new_sectin)

        addsec_heder = findViewById(R.id.addsec_heder)
        hedar_tital = addsec_heder.findViewById(R.id.hedar_tital)
        heder_bake = addsec_heder.findViewById(R.id.heder_bake)


         select_section = findViewById(R.id.select_section)
         btn_delet_section = findViewById(R.id.btn_delet_section)



        dialog2 =  Dialog(this)
        dialog2.setContentView(R.layout.dialog_delete_conferm)

        dialog2.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog2.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.window?.setGravity(Gravity.BOTTOM)

        prog_delete = dialog2.findViewById(R.id.prog_delete)
        btn_delete_cansel = dialog2.findViewById(R.id.btn_delete_cansel)
        btn_delete_conferm = dialog2.findViewById(R.id.btn_delete_conferm)
        delet_text = dialog2.findViewById(R.id.delet_text)
        delet_text.text = " سيتم حذف القسم مع المنتجات التي تنتمي اليه !"


        sec_parent = findViewById(R.id.sec_parent)
        sec_parent2 = findViewById(R.id.sec_parent2)

        select_section3 = findViewById(R.id.select_section3)

        btn_save_newNamw = findViewById(R.id.btn_save_newNamw)

        btn_save_newOrder = findViewById(R.id.btn_save_newOrder)
        chick_reorder_section = findViewById(R.id.chick_reorder_section)
        select_section3_name_order = findViewById(R.id.select_section3_name_order)
        select_section3_number_order = findViewById(R.id.select_section3_number_order)

        btn_save_newOrder_final = findViewById(R.id.btn_save_newOrder_final)

        input_newName = findViewById(R.id.input_newName)
        summary_secton_order =findViewById(R.id.summary_secton_order)
    }
}