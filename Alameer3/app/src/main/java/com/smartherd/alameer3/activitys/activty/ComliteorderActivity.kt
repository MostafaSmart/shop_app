package com.smartherd.alameer3.activitys.activty

import android.Manifest
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.BaseDirection
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.VerticalAlignment
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter.RUN_DIRECTION_RTL
import com.itextpdf.text.pdf.languages.ArabicLigaturizer


import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.MainActivity
import com.smartherd.alameer3.activitys.adapders.AdressAdapter
import com.smartherd.alameer3.activitys.adapders.NotListAdapter
import com.smartherd.alameer3.activitys.models.*
import com.smartherd.alameer3.activitys.serves.FCMService
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.awt.font.TextAttribute.RUN_DIRECTION_RTL
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ComliteorderActivity : AppCompatActivity() {
    private lateinit var show_adres1: TextView
    private lateinit var show_adres_2: TextView
    private lateinit var show_adres_3: TextView
    private lateinit var show_adres_4: TextView
    private lateinit var radioB_address: RadioButton
    private lateinit var radioB_payment: RadioButton
    private lateinit var btn_add_adress: LinearLayout
    private lateinit var show_all_adress: LinearLayout
    private lateinit var go_to_travel_opttions: Button
    private lateinit var back_complite_order: ImageView
    private lateinit var tital_complite_order: TextView
    private lateinit var dialog: Dialog
    private lateinit var com_main:LinearLayout
    private lateinit var city_spinner: Spinner
    private lateinit var country_spinner: Spinner
    private lateinit var btn_finsh_add_adress: Button
    private lateinit var input_fall_adress: com.google.android.material.textfield.TextInputEditText
    private lateinit var input_phone_order: com.google.android.material.textfield.TextInputEditText
    private lateinit var input_fall_name: com.google.android.material.textfield.TextInputEditText
    private var addresDitels = HashMap<String,String>()
    private lateinit var copy_ymem: Button
    private lateinit var pay1: LinearLayout
    private lateinit var copy_soydy: Button
    private lateinit var copy_doloar: Button
    private lateinit var show_total: TextView
    private lateinit var ch_pay_when_riv: CheckBox
    private lateinit var ch_pay_whth_korimi: CheckBox
    private lateinit var prog_finsh_order: ProgressBar
    private lateinit var send_the_final_requset: Button
    private lateinit var show_acount_inform: LinearLayout


    private lateinit var dialog2:Dialog
    private lateinit var show_name_finsh: TextView
    private lateinit var corimi_info: LinearLayout
    private lateinit var btn_finsh_confirm: Button
    private lateinit var totalprice_finsh: TextView
    private lateinit var show_adres_finsh: TextView
    private lateinit var show_price_tosel: TextView
    private lateinit var show_phone_tosel: TextView
    private lateinit var show_acount_inform2: LinearLayout
    private lateinit var text_korimi_masseg:TextView
    private lateinit var btn_copy_hasip:Button
    private lateinit var card_copy_hasip:LinearLayout
    private lateinit var text_copy_hasip:TextView

    private lateinit var text_hasib:TextView
    private lateinit var card_hasib:LinearLayout

    private lateinit var btn_use_old_adress:LinearLayout
    private lateinit var chick_save_address:CheckBox

    private lateinit var card_add_new_address:ScrollView
    private lateinit var card_show_old_address:RecyclerView

    private lateinit var add_name_hidin:TextView
    private lateinit var delevr_coust_hidden:TextView
    private val STORAGE_PERMISSION_CODE = 1

    private lateinit var dialog_not_avila: Dialog

    private lateinit var delet_text: TextView
    private lateinit var prog_delete: ProgressBar
    private lateinit var list_not_availab: ListView
    private lateinit var btn_order_anyway: android.widget.Button
    private lateinit var btn_cansol_order: android.widget.Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comliteorder)


        imelmnt()
        val total = intent.getStringExtra("totalPrice")

        btn_add_adress.setOnClickListener {
            templ()

        }

        btn_finsh_add_adress.setOnClickListener {
            AddAdress(total)
        }

        go_to_travel_opttions.setOnClickListener {

            if (show_all_adress.visibility != View.GONE){

                addresDitels.put("name",add_name_hidin.text.toString())
                addresDitels.put("contry",show_adres_3.text.toString())
                addresDitels.put("city",show_adres_2.text.toString())
                addresDitels.put("fulladdress",show_adres1.text.toString())
                addresDitels.put("phone",show_adres_4.text.toString())
                addresDitels.put("delevr_coust",delevr_coust_hidden.text.toString())

                addresDitels.put("totalprice",total!!)

            }
            else{
                Toast.makeText(this,"يجب تحديد عنوان اولاً",Toast.LENGTH_SHORT).show()

            }


            if(!addresDitels.isEmpty()){
                if(show_all_adress.visibility != View.GONE){
                    changeContent(total)
                    radioB_payment.isChecked = true

                    if(addresDitels.get("contry").toString() == "اب"){
                        pay1.visibility = View.VISIBLE

                    }

                }
                else{
                    Toast.makeText(this,"يجب تحديد عنوان اولاً",Toast.LENGTH_SHORT).show()

                }


            }
            else{
//                if (add_name_hidin.text!="عنوان" && show_adres_3.text!="null"){
//                    addresDitels.put("name",add_name_hidin.text.toString())
//                    addresDitels.put("contry",show_adres_3.text.toString())
//                    addresDitels.put("city",show_adres_2.text.toString())
//                    addresDitels.put("fulladdress",show_adres1.text.toString())
//                    addresDitels.put("phone",show_adres_4.text.toString())
//                    addresDitels.put("delevr_coust",delevr_coust_hidden.text.toString())
//
//                    addresDitels.put("totalprice",total!!)
//
//                    changeContent(total)
//                    radioB_payment.isChecked = true
//
//                    if(addresDitels.get("contry").toString() == "اب"){
//                        pay1.visibility = View.VISIBLE
//
//                    }
//
//                }
//                else{
//                    Toast.makeText(this,"يجب تحديد عنوان اولاً",Toast.LENGTH_SHORT).show()
//
//                }
            }
        }

        dialog2.setOnCancelListener {
            val muintin = Intent(this, MainActivity::class.java)
            muintin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(muintin)
        }




        btn_use_old_adress.setOnClickListener {

            var allAdress = kotlin.collections.ArrayList<Address>()
            var addID = kotlin.collections.ArrayList<String>()
            var flag = 0

            var selctedAddrs = HashMap<String,String>()
            Toast.makeText(this,"يجرى الانتظار",Toast.LENGTH_SHORT).show()

            card_show_old_address.layoutManager = LinearLayoutManager(this)

            var f_all_add = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("adress")
            f_all_add.get().addOnSuccessListener { add->
                if(add.documents.size>0){
                    card_add_new_address.visibility = View.GONE
                    add.documents.forEach {opadd->
                        allAdress.add(opadd.toObject(Address::class.java)!!)
                        addID.add(opadd.id)
                    }
                    card_show_old_address.visibility = View.VISIBLE
                    var addapter = AdressAdapter(dialog,allAdress,addID,selctedAddrs,flag)
                    card_show_old_address.adapter = addapter

                    dialog.show()

                    dialog.setOnCancelListener {

                        if (addapter.flag == 1){
                            show_adres1.text =addapter.selctedAddrs.get("fulladdress").toString()
                            show_adres_2.text = addapter.selctedAddrs.get("city").toString()
                            show_adres_3.text = addapter.selctedAddrs.get("contry").toString()
                            show_adres_4.text = addapter.selctedAddrs.get("phone").toString()
                            add_name_hidin.text = addapter.selctedAddrs.get("name").toString()
                            delevr_coust_hidden.text =addapter.selctedAddrs.get("delevr_coust").toString()

                            if(show_adres1.text!="null" && delevr_coust_hidden.text!="عنوان"){
                                card_show_old_address.visibility = View.GONE
                                show_all_adress.visibility = View.VISIBLE
                            }

                        }




                    }


                }
                else{
                    Toast.makeText(this,"لا يوجد عنواين قم باضافة عنوان جديد",Toast.LENGTH_LONG).show()
                }
            }


        }




    }

    fun changeContent(total: String?) {
        val contentLayout = findViewById<LinearLayout>(R.id.com_main)
        contentLayout.removeAllViews()
       val newContentView = layoutInflater.inflate(R.layout.finsh_order_2,null)

      contentLayout.addView(newContentView)

        imelmnt2(contentLayout)

        totalprice_finsh.text = total
        show_total.text = total

        selectPayMethod()

        copyAcountNumbers()

        finshDoingOrder()




    }

    private fun finshDoingOrder() {
        send_the_final_requset.setOnClickListener {

            if (ch_pay_whth_korimi.isChecked ==false && ch_pay_when_riv.isChecked==false) {
                Toast.makeText(this,"يجب اختيار طريقة الدفع .. يمكنك تغيرها لاحقا",Toast.LENGTH_SHORT).show()

            }
            else{

                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                prog_finsh_order.visibility = View.VISIBLE
                Toast.makeText(this, "يرجى الانتظار", Toast.LENGTH_SHORT).show()
                var firestore = FirebaseFirestore.getInstance()
                var dp = firestore.collection("calt").document(userId)
                dp.get().addOnSuccessListener {
                    if (it.exists()) {
                        var caltItem = it.toObject(Clat::class.java)!!
                        addresDitels.put("monyState",caltItem.state)
                        var orderCalt = Orders(
                            Calendar.getInstance().time,
                            addresDitels,
                            caltItem.products,
                            userId,
                            "new"
                        )

                        var count = 0
                        var notAA = kotlin.collections.ArrayList<ClatProdact>()
                        var notAvailbalProdact = kotlin.collections.ArrayList<List<String>>()
                        orderCalt.products.forEach { prodOrd ->
                            firestore.collection("prodacts2").document(prodOrd.prodid).get()
                                .addOnSuccessListener { proda ->
                                    var prodactItem = proda.toObject(Prodacts::class.java)!!
                                    var size = prodOrd.sizeprice.keys.firstOrNull().toString()
                                    var orderMuch = prodOrd.much

                                    var avalibleNow = prodactItem.contn.get(size).toString()


                                    var finalCount = ""
                                    var after_take =
                                        (avalibleNow.toInt() - orderMuch.toInt()).toString()

                                    if (after_take.toInt()< 0){
                                       var imagLink=""
                                        if (prodactItem.imageLinks.size>0){
                                            imagLink = prodactItem.imageLinks[0]
                                        }
                                        var not = listOf<String>(prodOrd.prodid,prodactItem.productName,imagLink,size,prodOrd.sizeprice.get(size).toString(),orderMuch,prodOrd.color)

                                        notAvailbalProdact.add(not)
                                        notAA.add(prodOrd)
                                    }

                                    count++
                                    if (count == orderCalt.products.size){
                                        if(notAvailbalProdact.size>0){
                                                Snackbar.make(prog_finsh_order,"يوجد منتجات نفذت كميتها حاليا",Snackbar.LENGTH_SHORT).show()

                                                var listAdapter = NotListAdapter(this, notAvailbalProdact)
                                                list_not_availab.adapter = listAdapter


                                            if (notAA.size == orderCalt.products.size){
                                                btn_order_anyway.visibility = View.GONE
                                            }
                                                dialog_not_avila.show()
                                                dialog_not_avila.setCancelable(false)



                                                btn_order_anyway.setOnClickListener {
                                                    prog_delete.visibility = View.VISIBLE
                                                   var newProd_oreder= orderCalt.products
                                                    notAA.forEach { aa ->
                                                        newProd_oreder.remove(aa)

                                                    }
                                                    var orderCalt22 = Orders(Calendar.getInstance().time, addresDitels, newProd_oreder, userId, "new")



                                                    compliteAllOrder(orderCalt22)
                                                }
                                                btn_cansol_order.setOnClickListener {
                                                    dialog_not_avila.cancel()
                                                    finish()

                                                }



                                            }
                                            else{

                                                compliteAllOrder(orderCalt)
                                            }


                                        }




                                }

                        }





                    }
                }

            }





        }
    }

    private fun compliteAllOrder(
        orderCalt22: Orders,
    ) {

        var firestore= FirebaseFirestore.getInstance()
        var dp11 = firestore.collection("prodacts2")
        var dp2 = firestore.collection("orders2")
        var  count = 0
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        var dp = firestore.collection("calt").document(userId)



        orderCalt22.products.forEach {prodOrd->

            dp11.document(prodOrd.prodid).get().addOnSuccessListener { proda ->
                var prodactItem = proda.toObject(Prodacts::class.java)!!
                var size = prodOrd.sizeprice.keys.firstOrNull().toString()
                var orderMuch = prodOrd.much

                var avalibleNow = prodactItem.contn.get(size).toString()


                var after_take = (avalibleNow.toInt() - orderMuch.toInt()).toString()

                prodactItem.contn.put(size, after_take)
                dp11.document(prodOrd.prodid).set(prodactItem).addOnSuccessListener {

                    count++
                    if(count==orderCalt22.products.size){
                        dp2.add(orderCalt22).addOnSuccessListener { de ->
                            dp.delete().addOnSuccessListener {
                                prog_finsh_order.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "على وشك الانتهاء",
                                    Toast.LENGTH_SHORT
                                ).show()

                                if (orderCalt22.info.get("pay").toString() != "عند الاستلام") {
                                    text_korimi_masseg.visibility = View.VISIBLE

                                    show_acount_inform2.visibility = View.VISIBLE

                                    var firestore =
                                        FirebaseFirestore.getInstance().collection("admin").document("info")
                                    firestore.get().addOnSuccessListener {
                                        if (it.get("hasib").toString().isNotEmpty()) {
                                            card_hasib.visibility = View.VISIBLE
                                            text_hasib.text = it.get("hasib").toString()
                                            makeNevgtionForAdmin(addresDitels.get("name").toString())

                                        } else {
                                            makeNevgtionForAdmin(addresDitels.get("name").toString())

                                        }


                                    }
                                } else {
                                    makeNevgtionForAdmin(addresDitels.get("name").toString())

                                }
                                btn_finsh_confirm.setOnClickListener {

                                    dialog2.cancel()

                                    //
                                }
                            }


                        }
                    }

                }
            }
        }



    }


    private fun copyAcountNumbers() {
        copy_ymem.setOnClickListener {
            copyCorimy("3060395852")
        }
        copy_soydy.setOnClickListener {
            copyCorimy("3062835914")
        }
        copy_doloar.setOnClickListener {
            copyCorimy("3060922453")
        }
    }

    private fun copyCorimy(textToCopy:String){
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = ClipData.newPlainText("Label", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this,"تم النسخ بنجاح",Toast.LENGTH_SHORT).show()


    }

    private fun makeNevgtionForAdmin(clintName:String){

        val adminid = "9cuzF1pt6DRAduNUd6ZGqWrGxeC2"
        val myid = "VfEvVuiJTdbCep7fQcYQVG6MivH3"


        val retrofit = Retrofit.Builder().baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val fcmApiService = retrofit.create(FCMService::class.java)


        var jsonObject = JSONObject()
        var notificationObject = JSONObject()

        jsonObject.put("condition", "'testadmin' in topics")
        jsonObject.put("priority", "high")

        notificationObject.put("title", "يوجد طلب جديد!")
        notificationObject.put("body", "من / "+clintName)

        jsonObject.put("notification", notificationObject)

        var jsonStrin = jsonObject.toString()
        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStrin)
        val call = fcmApiService.sendNotification(
            "application/json",
            "key=AAAAT05EJ0A:APA91bFl5oudhSbFyQxCNRpV6cx9jOciow-0HmdrRNyPGdtomKN3vRqbsXM9t0928Do4IwCLoqjsDUWVdoCNV2CIoNW_w3tHJqgkBY1Yf9TWUQgVzuwEvyTOYDZQgTHUt9OfvnMJOHVM",
            requestBody
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // يتم استدعاء هذه الوظيفة في حالة استلام استجابة صحيحة
                if (response.isSuccessful) {
                    Toast.makeText(this@ComliteorderActivity,"تم ارسال طلبك بنجاح شكرا لك",Toast.LENGTH_LONG).show()
                    dialog2.show()

                } else {
                    Toast.makeText(this@ComliteorderActivity,response.body().toString(),Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // يتم استدعاء هذه الوظيفة في حالة حدوث خطأ أثناء الاتصال
                // التعامل مع الخطأ هنا
                Toast.makeText(this@ComliteorderActivity,t.message.toString(),Toast.LENGTH_LONG).show()

            }
        })








    }

    private fun selectPayMethod() {
        ch_pay_when_riv.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ch_pay_whth_korimi.isChecked = false
                addresDitels.put("pay", "عند الاستلام")
                show_acount_inform.visibility = View.GONE
            } else {
                addresDitels.put("pay", "")
            }
        }

        ch_pay_whth_korimi.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ch_pay_when_riv.isChecked = false
                addresDitels.put("pay", "كريمي")

                FirebaseFirestore.getInstance().collection("admin").document("info").get().addOnSuccessListener {mos->
                    if(mos.get("hasib").toString().isNotEmpty()){
                        text_copy_hasip.text = mos.get("hasib").toString()
                        card_copy_hasip.visibility = View.VISIBLE

                        btn_copy_hasip.setOnClickListener {
                            copyCorimy(mos.get("hasib").toString())
                        }
                    }

                    show_acount_inform.visibility = View.VISIBLE



                }



            } else {
                addresDitels.put("pay", "")
            }
        }
    }


    private fun AddAdress(total:String?) {
        if (input_fall_name.text.toString().isNotEmpty() && country_spinner.selectedItem != null
            && city_spinner.selectedItem != null && input_fall_adress.text.toString().isNotEmpty()
            && input_phone_order.text.toString().isNotEmpty()
        ) {

            addresDitels.put("name", input_fall_name.text.toString())
            addresDitels.put("contry", country_spinner.selectedItem.toString())
            addresDitels.put("city", city_spinner.selectedItem.toString())
            addresDitels.put("fulladdress", input_fall_adress.text.toString())
            addresDitels.put("phone", input_phone_order.text.toString())
            addresDitels.put("totalprice",total!!)


            Toast.makeText(this,"يجرى الانتظار",Toast.LENGTH_SHORT).show()
            if(chick_save_address.isChecked){

               var  newAdreRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("adress").document()

                var adress = Address(addresDitels.get("name")!!,addresDitels.get("contry")!!,addresDitels.get("city")!!,addresDitels.get("fulladdress")!!,addresDitels.get("phone")!!,addresDitels.get("delevr_coust")!!)

                newAdreRef.set(adress).addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "تمت الاضافة بنجاح يمكنك المواصلة لأكمال عملية الشراء",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.cancel()
                    show_all_adress.visibility = View.VISIBLE
                    show_adres1.text = addresDitels.get("fulladdress").toString()
                    show_adres_2.text = addresDitels.get("city").toString()
                    show_adres_3.text = addresDitels.get("contry").toString()
                    show_adres_4.text = addresDitels.get("phone").toString()
                    add_name_hidin.text = addresDitels.get("name").toString()
                    card_add_new_address.visibility = View.GONE

                }
            }
            else{
                Toast.makeText(
                    this,
                    "تمت الاضافة بنجاح يمكنك المواصلة لأكمال عملية الشراء",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.cancel()
                show_all_adress.visibility = View.VISIBLE
                show_adres1.text = addresDitels.get("fulladdress").toString()
                show_adres_2.text = addresDitels.get("city").toString()
                show_adres_3.text = addresDitels.get("contry").toString()
                show_adres_4.text = addresDitels.get("phone").toString()
                add_name_hidin.text = addresDitels.get("name").toString()
                card_add_new_address.visibility = View.GONE

            }





        }
    }
    


    fun templ(){


        var firestore = FirebaseFirestore.getInstance().collection("city").document("data")
        firestore.get().addOnSuccessListener {
            var cantory = ArrayList<String>()
            var dataCity =  it.toObject(Citys::class.java)!!
            dataCity.contryprice.keys.forEach { cont->
                cantory.add(cont)
            }
            var adapter = ArrayAdapter(this, R.layout.spinner_item, cantory)
            country_spinner.adapter = adapter

            getCitysFromContorySelect(dataCity)
            card_show_old_address.visibility = View.GONE
            card_add_new_address.visibility = View.VISIBLE
            dialog.show()






        }


    }

    private fun getCitysFromContorySelect(dataCity: Citys) {
        country_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var contrySelect = country_spinner.selectedItem.toString()
                var city = dataCity.cit.get(contrySelect)
                var dd = dataCity.contryprice.get(contrySelect).toString()
                addresDitels.put("delevr_coust",dd)

                var adapter2 = ArrayAdapter(
                    this@ComliteorderActivity,
                    R.layout.spinner_item,
                    city!!
                )

                city_spinner.adapter = adapter2


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun imelmnt() {
        com_main =findViewById(R.id.com_main)
        show_adres1 = com_main.findViewById(R.id.show_adres1)
        show_adres_2 =com_main.findViewById(R.id.show_adres_2)
        show_adres_3 = com_main.findViewById(R.id.show_adres_3)
        show_adres_4 = com_main.findViewById(R.id.show_adres_4)
        add_name_hidin = com_main.findViewById(R.id.add_name_hidin)
        delevr_coust_hidden = com_main.findViewById(R.id.delevr_coust_hidden)

        radioB_address = findViewById(R.id.radioB_address)
        radioB_payment = findViewById(R.id.radioB_payment)
        btn_add_adress = com_main.findViewById(R.id.btn_add_adress)
        btn_use_old_adress = com_main.findViewById(R.id.btn_use_old_adress)
        show_all_adress = com_main.findViewById(R.id.show_all_adress)
        back_complite_order = findViewById(R.id.back_complite_order)
        tital_complite_order = findViewById(R.id.tital_complite_order)
        go_to_travel_opttions = com_main.findViewById(R.id.go_to_travel_opttions)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_address)

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

        city_spinner = dialog.findViewById(R.id.city_spinner)
        country_spinner = dialog.findViewById(R.id.country_spinner)
        input_fall_adress = dialog.findViewById(R.id.input_fall_adress)
        input_fall_name = dialog.findViewById(R.id.input_fall_name)

        input_phone_order = dialog.findViewById(R.id.input_phone_order)

        btn_finsh_add_adress = dialog.findViewById(R.id.btn_finsh_add_adress)

        chick_save_address = dialog.findViewById(R.id.chick_save_address)
        card_show_old_address = dialog.findViewById(R.id.card_show_old_address)

        card_add_new_address = dialog.findViewById(R.id.card_add_new_address)





        dialog2 = Dialog(this)
        dialog2.setContentView(R.layout.bills_out_orders)

        dialog2.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog2.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.window?.setGravity(Gravity.BOTTOM)
		corimi_info = dialog2.findViewById(R.id.corimi_info)
		show_name_finsh = dialog2.findViewById(R.id.show_name_finsh)
		totalprice_finsh = dialog2.findViewById(R.id.totalprice_finsh)
		show_adres_finsh = dialog2.findViewById(R.id.show_adres_finsh)
		show_price_tosel = dialog2.findViewById(R.id.show_price_tosel)
		show_phone_tosel = dialog2.findViewById(R.id.show_phone_tosel)
		btn_finsh_confirm = dialog2.findViewById(R.id.btn_finsh_confirm)
		show_acount_inform2 = dialog2.findViewById(R.id.show_acount_inform2)


        card_hasib =  dialog2.findViewById(R.id.card_hasib)
        text_hasib = dialog2.findViewById(R.id.text_hasib)
        text_korimi_masseg = dialog2.findViewById(R.id.text_korimi_masseg)



        dialog_not_avila =  Dialog(this)
        dialog_not_avila.setContentView(R.layout.dialog_not_avi_prodacts)

        dialog_not_avila.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog_not_avila.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog_not_avila.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog_not_avila.window?.setGravity(Gravity.BOTTOM)


		delet_text = dialog_not_avila.findViewById(R.id.delet_text)
		prog_delete = dialog_not_avila.findViewById(R.id.prog_delete)
		btn_order_anyway = dialog_not_avila.findViewById(R.id.btn_order_anyway)
		btn_cansol_order = dialog_not_avila.findViewById(R.id.btn_cansol_order)
		list_not_availab = dialog_not_avila.findViewById(R.id.list_not_availab)


    }
    private fun imelmnt2(contentLayout: LinearLayout) {
        pay1 = contentLayout.findViewById(R.id.pay1)
        copy_ymem = contentLayout.findViewById(R.id.copy_ymem)
        copy_soydy = contentLayout.findViewById(R.id.copy_soydy)
        show_total = contentLayout.findViewById(R.id.show_total)
        copy_doloar = contentLayout.findViewById(R.id.copy_doloar)
        ch_pay_when_riv = contentLayout.findViewById(R.id.ch_pay_when_riv)
        prog_finsh_order = contentLayout.findViewById(R.id.prog_finsh_order)
        ch_pay_whth_korimi = contentLayout.findViewById(R.id.ch_pay_whth_korimi)
        show_acount_inform = contentLayout.findViewById(R.id.show_acount_inform)
        send_the_final_requset = contentLayout.findViewById(R.id.send_the_final_requset)


        btn_copy_hasip = contentLayout.findViewById(R.id.btn_copy_hasip)

        card_copy_hasip = contentLayout.findViewById(R.id.card_copy_hasip)
        card_copy_hasip.visibility = View.GONE
        text_copy_hasip = contentLayout.findViewById(R.id.text_copy_hasip)


    }

}