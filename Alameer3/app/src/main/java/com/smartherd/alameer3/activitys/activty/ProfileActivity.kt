package com.smartherd.alameer3.activitys.activty

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.BaseDirection
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.text.pdf.languages.ArabicLigaturizer


import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.models.Prodacts
import com.smartherd.alameer3.activitys.models.Users
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class ProfileActivity : AppCompatActivity() {
    private lateinit var on_nave: Button
    private lateinit var add_prod: Button
    private lateinit var add_Logout: Button
    private lateinit var on_stop_prod: Button
    private lateinit var on_banal_upd: Button
    private lateinit var on_update_dolar: Button
    private lateinit var add_delete_acount: Button
    private lateinit var prog_profile:ProgressBar
    private lateinit var linearLayout: LinearLayout
    private lateinit var btn_updite_profile: Button
    private lateinit var show_page_loaction: Spinner
    private lateinit var cardView: androidx.cardview.widget.CardView
    private lateinit var on_add_section:Button
    private lateinit var constraintLayout: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var show_page_name: com.google.android.material.textfield.TextInputEditText
    private lateinit var show_page_email: com.google.android.material.textfield.TextInputEditText
    private lateinit var show_page_phone: com.google.android.material.textfield.TextInputEditText
    private val countries = listOf("شمال", "جنوب")

    private lateinit var dialog: Dialog
    private lateinit var upDolar: EditText
    private lateinit var downDolar: EditText
    private lateinit var prog_dolar: ProgressBar
    private lateinit var btn_update_dolar: Button
    private lateinit var back_profile:ImageView

    private lateinit var dialog2: Dialog
    private lateinit var prog_delete: ProgressBar
    private lateinit var btn_delete_cansel: Button
    private lateinit var btn_delete_conferm: Button
    private lateinit var dialog3:Dialog

    private lateinit var btn_number_vald:Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        imelmnt()
        check()
        detUSerData()
        clickLisener()


        on_nave.setOnClickListener {
            clearAddes()
        }

    }





    private fun updateUserInfo() {
        if (show_page_name.text.toString().isNotEmpty() && show_page_phone.text.toString()
                .isNotEmpty()
        ) {

            if (show_page_phone.text.toString().startsWith("7")&& show_page_phone.text.toString().length == 9)
            {

                if (show_page_name.text.toString().length>7){
                    Toast.makeText(this, "يرجى الانتظار", Toast.LENGTH_SHORT).show()

                    prog_profile.visibility = View.VISIBLE
                    var userId = FirebaseAuth.getInstance().currentUser!!.uid
                    var name = show_page_name.text.toString()
                    var email = show_page_email.text.toString()
                    var state = show_page_loaction.selectedItem.toString()
                    var phone = show_page_phone.text.toString()

                    FirebaseMessaging.getInstance().token.addOnSuccessListener { taskToken ->

                        var newuser = Users(name, email, phone, state,taskToken.toString(),"0")
                        var firestore = FirebaseFirestore.getInstance()
                        firestore.collection("users").document(userId).set(newuser).addOnSuccessListener {
                            prog_profile.visibility = View.GONE
                            Toast.makeText(this, "تم التحديث بنجاح", Toast.LENGTH_SHORT).show()
                            detUSerData()
                        }


                    }


                }
                else{
                    Toast.makeText(this,"يرجى ادخال الاسم الرباعي",Toast.LENGTH_SHORT).show()
                    show_page_name.error="يجب ادخال اسمك الرباعي"
                    show_page_name.requestFocus()
                }




            }
            else{
                Toast.makeText(this,"رقم الهاتف غير صالح",Toast.LENGTH_SHORT).show()
                show_page_phone.error="يجب ان يكون 9 ارقام"
                show_page_phone.requestFocus()

            }




        }
        else{
            Toast.makeText(this,"يجب كتابة بيانات صحيحة",Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteAcount(){
        prog_delete.visibility = View.VISIBLE
        var userid = FirebaseAuth.getInstance().currentUser!!.uid
        var dp = FirebaseFirestore.getInstance().collection("users")

        dp.document(userid).delete().addOnSuccessListener {
            FirebaseAuth.getInstance().currentUser!!.delete().addOnSuccessListener {
                prog_delete.visibility = View.GONE
                Toast.makeText(this,"تم حذف الحساب بنجاح",Toast.LENGTH_SHORT).show()
               var  intent = Intent(this,LoginSiginpActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }



    }

    private fun clickLisener(){

        add_prod.setOnClickListener {
            val intent = Intent(this,AddprodactActivity::class.java)
            startActivity(intent)
        }

        on_update_dolar.setOnClickListener {
            openDialogDolar()
        }
        btn_update_dolar.setOnClickListener {
            updateDolarValue()
        }
        on_add_section.setOnClickListener {
            var intent = Intent(this,AddsectionActivity::class.java)
            startActivity(intent)

        }
        on_stop_prod.setOnClickListener {
            var intent = Intent(this,MoreActivity::class.java)
            intent.putExtra("hidden","true")
            startActivity(intent)
        }

        add_Logout.setOnClickListener {

                        FirebaseAuth.getInstance().signOut()
            reload()


        }
        back_profile.setOnClickListener {
            finish()
        }
        btn_updite_profile.setOnClickListener {
            updateUserInfo()
        }
        on_banal_upd.setOnClickListener {
            var intent = Intent(this,BanalsCingActivity::class.java)
            startActivity(intent)
        }

        add_delete_acount.setOnClickListener {
            dialog2.show()
        }

        btn_delete_conferm.setOnClickListener {
            deleteAcount()
        }

        btn_delete_cansel.setOnClickListener {
            dialog2.cancel()
        }







    }






    private fun openDialogDolar() {
        Toast.makeText(this, "يرجى الانتظار", Toast.LENGTH_SHORT).show()
        var fires = FirebaseFirestore.getInstance().collection("admin").document("dolarV")
        fires.get().addOnSuccessListener {
            downDolar.setText(it.get("dolardown").toString())
            upDolar.setText(it.get("dolarup").toString())
            dialog.show()

        }
    }

    private fun updateDolarValue(){

        if(upDolar.text.toString().isNotEmpty() && downDolar.text.toString().isNotEmpty()){

            prog_dolar.visibility = View.VISIBLE
            val upD = upDolar.text.toString()
            val downD = downDolar.text.toString()

            var hashMap = HashMap<String,String>()
            hashMap.put("dolarup",upD)
            hashMap.put("dolardown",downD)

            val fire = FirebaseFirestore.getInstance().collection("admin")
            fire.document("dolarV").set(hashMap).addOnSuccessListener {
                Toast.makeText(this,"تم التحديث بنجاح",Toast.LENGTH_SHORT).show()
                prog_dolar.visibility = View.GONE
                dialog.cancel()
            }


        }


    }

    private fun check(){

        if(FirebaseAuth.getInstance().currentUser!!.uid=="9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || FirebaseAuth.getInstance().currentUser!!.uid=="VfEvVuiJTdbCep7fQcYQVG6MivH3")
        {
            add_prod.visibility = View.VISIBLE
            on_update_dolar.visibility = View.VISIBLE
            on_nave.visibility = View.VISIBLE
            on_banal_upd.visibility = View.VISIBLE
            on_add_section.visibility = View.VISIBLE
            on_stop_prod.visibility = View.VISIBLE

            btn_number_vald.visibility = View.VISIBLE


            btn_number_vald.setOnClickListener {
                var intent = Intent(this,PhoneTowActivity::class.java)
                startActivity(intent)
            }

            var btn_number_vald22 = findViewById<Button>(R.id.btn_number_vald22)

            btn_number_vald22.visibility = View.VISIBLE
            btn_number_vald22.setOnClickListener {
                var intent = Intent(this,PhoneLoginActivity::class.java)
                startActivity(intent)
            }


        }


    }

    private fun detUSerData(){
        val firestore = FirebaseFirestore.getInstance()

        var dp = firestore.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
        dp.get().addOnSuccessListener {
            show_page_name.setText( it.get("name").toString())
            show_page_email.setText(it.get("email").toString())
            show_page_phone.setText(it.get("phone").toString())
            if(it.get("state").toString() =="جنوب"){
                show_page_loaction.setSelection(1)
            }
            else{
                show_page_loaction.setSelection(0)

            }



        }



    }


    private fun clearAddes(){


        var auth = FirebaseAuth.getInstance()
        if (auth.currentUser!=null){
            if(auth.currentUser!!.uid == "9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || auth.currentUser!!.uid =="VfEvVuiJTdbCep7fQcYQVG6MivH3"){

                val show_count_text = dialog3.findViewById<TextView>(R.id.show_count_text);
                val prog_panal_serch = dialog3.findViewById<ProgressBar>(R.id.prog_panal_serch);

                dialog3.show()
                val db = FirebaseFirestore.getInstance()
                val productsCollection = db.collection("prodacts2")
                val currentDate = Calendar.getInstance().time
                Toast.makeText(this,"جاري البحث عن تخفيضات منتهية",Toast.LENGTH_SHORT).show()
                 var descount_sizes = LinkedHashMap<String,String>()

                var count = 0
                var count2 = 0
                var countNull = 0
                var countAll = 0
                productsCollection.get().addOnSuccessListener { querySnapshot ->

                    querySnapshot.documents.forEach {doc->
                        var data=  doc.toObject(Prodacts::class.java)!!
                        countAll++
                        if(data.expirDesc!=null){
                            if(currentDate >=data.expirDesc){
                                count++
                            }

                        }
                        else{
                            countNull++
                        }

                    }

                    if(countAll ==countNull ){
                        dialog3.cancel()
                        Toast.makeText(this,"تم بنجاح",Toast.LENGTH_SHORT).show()

                    }

                    querySnapshot.documents.forEach {doc->
                        var data=  doc.toObject(Prodacts::class.java)!!

                        if(data.expirDesc!=null){
                            if(currentDate >=data.expirDesc){

                                data.discount = descount_sizes
                                data.expirDesc = null
                                db.collection("prodacts2").document(doc.id).set(data).addOnSuccessListener {
                                    count2++
                                    show_count_text.text = count2.toString()

                                    if(count2 >=count){
                                        dialog3.cancel()
                                        Toast.makeText(this,"تم بنجاح",Toast.LENGTH_SHORT).show()
                                    }

                                }
                            }

                        }

                    }


                }


            }
        }


    }


    private fun imelmnt() {
        on_nave = findViewById(R.id.on_nave)
        cardView = findViewById(R.id.cardView)
        add_prod = findViewById(R.id.add_prod)
        add_Logout = findViewById(R.id.add_Logout)
        linearLayout = findViewById(R.id.linearLayout)
        on_stop_prod = findViewById(R.id.on_stop_prod)
        on_banal_upd = findViewById(R.id.on_banal_upd)
        show_page_name = findViewById(R.id.show_page_name)
        show_page_email = findViewById(R.id.show_page_email)
        show_page_phone = findViewById(R.id.show_page_phone)
        on_update_dolar = findViewById(R.id.on_update_dolar)
        constraintLayout = findViewById(R.id.constraintLayout)
        prog_profile = findViewById(R.id.prog_profile)
        add_delete_acount = findViewById(R.id.add_delete_acount)
        show_page_loaction = findViewById(R.id.show_page_loaction)
        btn_updite_profile = findViewById(R.id.btn_updite_profile)
        on_add_section = findViewById(R.id.on_add_section)

        btn_number_vald  =findViewById(R.id.btn_number_vald)
        back_profile = findViewById(R.id.back_profile)

        val Loction = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        Loction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        show_page_loaction.adapter = Loction

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_dolar_update)

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)

		upDolar = dialog.findViewById(R.id.upDolar)
		downDolar = dialog.findViewById(R.id.downDolar)
		prog_dolar = dialog.findViewById(R.id.prog_dolar)
		btn_update_dolar = dialog.findViewById(R.id.btn_update_dolar)






        dialog2 =  Dialog(this)
        dialog2.setContentView(R.layout.dialog_delete_conferm)

        dialog2.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog2.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.window?.setGravity(Gravity.BOTTOM)

		prog_delete = dialog2.findViewById(R.id.prog_delete)
		btn_delete_cansel = dialog2.findViewById(R.id.btn_delete_cansel)
		btn_delete_conferm = dialog2.findViewById(R.id.btn_delete_conferm)





        dialog3 = Dialog(this)
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog3.setContentView(R.layout.dialog_panal_clear)





        dialog3.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog3.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog3.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog3.window?.setGravity(Gravity.CENTER)






    }
    private fun reload() {
        val muintin = Intent(this, LoginSiginpActivity::class.java)
        muintin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(muintin)
    }
}