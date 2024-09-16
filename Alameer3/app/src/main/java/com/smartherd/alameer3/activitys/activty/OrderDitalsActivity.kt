package com.smartherd.alameer3.activitys.activty

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
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
import com.itextpdf.text.pdf.languages.ArabicLigaturizer

import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.adapders.SalahAdapter
import com.smartherd.alameer3.activitys.models.ClatProdact
import com.smartherd.alameer3.activitys.models.Orders
import com.smartherd.alameer3.activitys.models.Prodacts
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
import java.io.File
import java.io.FileOutputStream
import java.util.*

class OrderDitalsActivity : AppCompatActivity() {
    private lateinit var cost_city: TextView
    private lateinit var delete_state: Button
    private lateinit var cost_phone: TextView
    private lateinit var updeite_state: Button
    private lateinit var cost_contry: TextView
    private lateinit var cost_adress: TextView
    private lateinit var costmor_name: TextView
    private lateinit var btn_cansel_order: Button
    private lateinit var deel_done_rb: RadioButton
    private lateinit var costmor_pay_way: TextView
    private lateinit var back_orderDitel: ImageView
    private lateinit var cost_total_price: TextView
    private lateinit var RG_order_updiet: RadioGroup
    private lateinit var ad_new_order_rb: RadioButton
    private lateinit var btn_call_the_costmer: Button
    private lateinit var prog_order_detel: ProgressBar
    private lateinit var list_order_detels: androidx.recyclerview.widget.RecyclerView
    private var state =""

    private lateinit var dialog: Dialog
    private lateinit var prog_delete: ProgressBar
    private lateinit var btn_delete_cansel: Button
    private lateinit var btn_delete_conferm: Button
    private lateinit var btn_bils_order_out:Button
    private lateinit var costmor_monyState:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_ditals)


        imelmnt()

        val orderid = intent.getStringExtra("orderid")

        list_order_detels.layoutManager = LinearLayoutManager(this)
        chick()


        getOrdersDetels(orderid)

        btn_cansel_order.setOnClickListener {
            dialog.show()
        }
        btn_delete_cansel.setOnClickListener {
            dialog.cancel()
        }







    }

    private fun chingOrderState(order: Orders,dp1: DocumentReference) {
        RG_order_updiet.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.deel_done_rb -> {
                    state = "half"
                }
                R.id.ad_new_order_rb -> {
                    state = "new"
                }
            }
        }
        updeite_state.setOnClickListener {
            prog_order_detel.visibility = View.VISIBLE
            var newOrder =Orders(order.date,order.info,order.products,order.userId,state)
            dp1.set(newOrder).addOnSuccessListener {
                sentNotevection(order.userId)

            }

        }



    }

    private fun sentNotevection(userID:String){
        val retrofit = Retrofit.Builder().baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val fcmApiService = retrofit.create(FCMService::class.java)
        var jsonObject = JSONObject()
        var notificationObject = JSONObject()
        notificationObject.put("title","تمت مراجعة طلبيتك!")
        notificationObject.put("body","عزيزي العميل لقد تم مراجعة طلبك ويتم الان تجهزيها ,شكراً لك")
        jsonObject.put("notification",notificationObject)

        var firestor = FirebaseFirestore.getInstance()
        firestor.collection("users").document(userID).get().addOnSuccessListener { tt->
            if (tt.exists()){
                var token = tt.get("token").toString()
                jsonObject.put("to",token)
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
                            Toast.makeText(this@OrderDitalsActivity,"تم تحديث حالة الطلب",Toast.LENGTH_LONG).show()
                            prog_order_detel.visibility = View.GONE

                        } else {
                            Toast.makeText(this@OrderDitalsActivity,response.body().toString(),Toast.LENGTH_LONG).show()
                            prog_order_detel.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // يتم استدعاء هذه الوظيفة في حالة حدوث خطأ أثناء الاتصال
                        // التعامل مع الخطأ هنا
                        Toast.makeText(this@OrderDitalsActivity,t.message.toString(),Toast.LENGTH_LONG).show()
                        prog_order_detel.visibility = View.GONE
                    }
                })



            }

        }



    }
    private fun getOrdersDetels(orderid: String?) {
        var firestore = FirebaseFirestore.getInstance()

        var dp1 = firestore.collection("orders2").document(orderid!!)
        dp1.get().addOnSuccessListener { ord ->
            if (ord.exists()) {
                var order = ord.toObject(Orders::class.java)!!

                showInfo(order)
                val storedTimestamp = ord.getTimestamp("date")



                list_order_detels.adapter = SalahAdapter(null, order.products, null)

                chingOrderState(order,dp1)

                deleteOrederCoustmer(storedTimestamp, order, firestore, orderid)



                btn_bils_order_out.setOnClickListener {

                    Toast.makeText(this,"يرجى الانتظار",Toast.LENGTH_SHORT).show()
                    prog_order_detel.visibility = View.VISIBLE
                    var addresDitels = HashMap<String,String>()

                    addresDitels.put("ordid",orderid)

                    addresDitels.put("name",order.info.get("name").toString())
                    addresDitels.put("city", order.info.get("city").toString())

                    addresDitels.put("phone", order.info.get("phone").toString())

                    addresDitels.put("contry", order.info.get("contry").toString())
                    addresDitels.put("fulladdress", order.info.get("fulladdress").toString())
                    addresDitels.put("delevr_coust", order.info.get("delevr_coust").toString())

                    addresDitels.put("pay", order.info.get("pay").toString())
                    addresDitels.put("totalprice", order.info.get("totalprice").toString())

                    if (order.info.containsKey("monyState")){
                        addresDitels.put("monyState", order.info.get("monyState").toString())

                    }

                    addresDitels.put("date",order.date.toString())







                    var count = 0

                    var detelsList = kotlin.collections.ArrayList<kotlin.collections.List<String>>()

                    order.products.forEach {prd->
                        firestore.collection("prodacts2").document(prd.prodid).get().addOnSuccessListener { proda ->

                            var prodactItem = proda.toObject(Prodacts::class.java)!!
                            var size = prd.sizeprice.keys.firstOrNull().toString()
                            var price = prd.sizeprice.get(size).toString()
                            var orderMuch = prd.much
                            var color = prd.color

                            detelsList.add(listOf(prodactItem.productName,price,size,orderMuch,color))

                            count++
                            if (count == order.products.size){

                                Toast.makeText(this,"على وشك الانتهاء",Toast.LENGTH_SHORT).show()

                                createPDF(addresDitels,detelsList,orderid!!)



                            }


                        }


                    }

                }

            }


        }
    }



    private fun createPDF(orderCalt: HashMap<String,String>,detelsList:kotlin.collections.ArrayList<kotlin.collections.List<String>>,uid:String) {

        var name = orderCalt.get("name").toString()
        var contry =
            orderCalt.get("contry").toString() + " - " + orderCalt.get("city").toString()
        var add = orderCalt.get("fulladdress").toString()
        var x_phone = orderCalt.get("phone").toString()
        var payMethod = orderCalt.get("pay").toString()

        var monyState = ""
        if (orderCalt.containsKey("monyState")){
            monyState = orderCalt.get("monyState").toString()
        }

        var delevr_coust = orderCalt.get("delevr_coust").toString()
        var totalprice = orderCalt.get("totalprice").toString()
        var date = orderCalt.get("date").toString()

        try {
//            val cacheDir = this.cacheDir
//            var path = Environment.getExternalStorageDirectory().toString() + File.separator + "ecm1.pdf"
            val fileName = "ecm1.pdf"
            val path = File(cacheDir, fileName).absolutePath

            val pdfWriter = PdfWriter(path)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            document.setMargins(20f, 20f, 20f, 20f);
            document.setBorder(SolidBorder(4f));

            var languageProcessor = ArabicLigaturizer()

            val assetManager = this.assets


            val fontInputStream = assetManager.open("103-Tahoma.ttf")
            val fontFile = File(this.cacheDir, "103-Tahoma.ttf")
            val outputStream = FileOutputStream(fontFile)
            fontInputStream.copyTo(outputStream)
            outputStream.close()
            fontInputStream.close()

            val logoInputStream = assetManager.open("logo.png")
            val logoFile = File(this.cacheDir, "logo.png")
            val logoStream = FileOutputStream(logoFile)
            logoInputStream.copyTo(logoStream)
            logoStream.close()
            logoInputStream.close()

            var dataimage = ImageDataFactory.create(logoFile.absolutePath)
            var img = Image(dataimage).setHeight(100f).setWidth(100f).setHorizontalAlignment(
                HorizontalAlignment.CENTER)
            document.add(img)



            val arabicFont = PdfFontFactory.createFont(fontFile.absolutePath, PdfEncodings.IDENTITY_H)
            val arabicText = "فاتورة رقمية من الامير ستور"


            // إنشاء فقرة مع النص العربي
            val paragraph = Paragraph(languageProcessor.process(arabicText))
                .setFont(arabicFont)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20f)
                .setBaseDirection(BaseDirection.RIGHT_TO_LEFT) // محاذاة النص لليمين

            // إضافة الفقرة إلى الوثيقة
            document.add(paragraph)

            document.add(
                Paragraph(languageProcessor.process("  رمز الطلب:\t ${uid} ")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.LEFT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))


            document.add(
                Paragraph(languageProcessor.process("الاسم :\t ${name} ")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.RIGHT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))
            document.add(
                Paragraph(languageProcessor.process("العنوان :\t ${contry} / ${add}")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.RIGHT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))
            document.add(
                Paragraph(languageProcessor.process("الهاتف :\t ${x_phone} ")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.RIGHT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))
            document.add(
                Paragraph(languageProcessor.process("طريقة الدفع  :\t ${payMethod} ")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.RIGHT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))
            document.add(
                Paragraph(languageProcessor.process("العملة  :\t ${monyState} ")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.RIGHT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))

            document.add(
                Paragraph(languageProcessor.process("  تكلفة التوصيل:\t ${delevr_coust} ")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.RIGHT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))


            document.add(Paragraph(" "))


            val table = Table(floatArrayOf(6f, 6f, 6f,6f,6f)).setBaseDirection(BaseDirection.RIGHT_TO_LEFT)
            table.setTextAlignment(TextAlignment.CENTER)
            table.setHorizontalAlignment(HorizontalAlignment.CENTER)

            table.addCell(
                Paragraph(languageProcessor.process("  المنتج")).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))
            table.addCell(
                Paragraph(languageProcessor.process("  السعر")).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))
            table.addCell(
                Paragraph(languageProcessor.process("  المقاس")).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))


            table.addCell(
                Paragraph(languageProcessor.process("  الكمية")).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))

            table.addCell(
                Paragraph(languageProcessor.process("  اللون")).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))



            detelsList.forEach { prof->
                for (data in prof) {
                    table.addCell(
                        Paragraph(languageProcessor.process(data)).setFontSize(14f).setFont(arabicFont).setBaseDirection(
                            BaseDirection.RIGHT_TO_LEFT))
                }
            }
            document.add(Paragraph(" "))

            table.addCell(
                Paragraph(languageProcessor.process("  الاجمالي ")).setFont(arabicFont).setFontSize(17f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))
            table.addCell(
                Paragraph(languageProcessor.process(totalprice)).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))



            document.add(table)

            document.add(Paragraph(" "))

            document.add(
                Paragraph(languageProcessor.process("  date:\t ${date} ")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.RIGHT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))




            // إغلاق الوثيقة
            document.close()

            val pdfFile = File(path)
            prog_order_detel.visibility = View.GONE
            showOpenOptions(pdfFile)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showOpenOptions(file: File) {
        val openIntent = Intent(Intent.ACTION_VIEW)
        openIntent.type = "application/pdf"

        // تحديد ملف PDF للفتح باستخدام FileProvider
        val fileUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(this, packageName  + ".fileprovider", file)
        } else {
            Uri.fromFile(file)
        }

        openIntent.setDataAndType(fileUri, "application/pdf")

        // منح الأذونات المؤقتة للوصول إلى الملف
        openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // استدعاء نافذة الفتح
        val chooserIntent = Intent.createChooser(openIntent, "فتح الملف باستخدام:")
        startActivity(chooserIntent)
    }

    private fun deleteOrederCoustmer(
        storedTimestamp: Timestamp?,
        order: Orders,
        firestore: FirebaseFirestore,
        orderid: String?
    ) {
        btn_delete_conferm.setOnClickListener {
            prog_delete.visibility = View.VISIBLE
            val currentTime = Calendar.getInstance().time
            val storedTime = storedTimestamp?.toDate()?.time
            val currentTimeMillis = currentTime.time


            val timeDifferenceMillis = currentTimeMillis - storedTime!!

            val hoursDifference = timeDifferenceMillis / (1000 * 60 * 60)

            if (hoursDifference > 6) {
                Toast.makeText(
                    this,
                    "لا يمكن الغاء الطلب الذي مر علية اكثر من 6 ساعات ",
                    Toast.LENGTH_LONG
                ).show()
                prog_delete.visibility = View.GONE
                dialog.cancel()
            } else {
                var count = 0
                order.products.forEach { itm ->
                    firestore.collection("prodacts2").document(itm.prodid).get()
                        .addOnSuccessListener { prod ->
                            if (prod.exists()) {
                                var prodactItem = prod.toObject(Prodacts::class.java)!!
                                var newCount = prodactItem.contn.get(
                                    itm.sizeprice.keys.firstOrNull().toString()
                                ).toString()
                                var newCount2 = (newCount.toInt() + itm.much.toInt()).toString()
                                prodactItem.contn.put(
                                    itm.sizeprice.keys.firstOrNull().toString(),
                                    newCount2
                                )

                                firestore.collection("prodacts2").document(itm.prodid)
                                    .set(prodactItem).addOnSuccessListener {
                                        count++

                                        if (count == order.products.size) {
                                            firestore.collection("orders2").document(orderid!!).delete()
                                                .addOnSuccessListener {
                                                    prog_delete.visibility = View.GONE
                                                    dialog.cancel()
                                                    Toast.makeText(
                                                        this,
                                                        "تم الغاء الطلب بنجاح",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    finish()
                                                }

                                        }

                                    }
                            }

                        }

                }

            }


        }
    }

    private fun chick(){

        if(FirebaseAuth.getInstance().currentUser!!.uid=="9cuzF1pt6DRAduNUd6ZGqWrGxeC2"
            || FirebaseAuth.getInstance().currentUser!!.uid=="VsEoPCyXGGdIqnrVa1S1hp2b4w82"
            || FirebaseAuth.getInstance().currentUser!!.uid=="VfEvVuiJTdbCep7fQcYQVG6MivH3"){
            updeite_state.visibility = View.VISIBLE
            RG_order_updiet.visibility = View.VISIBLE
            btn_call_the_costmer.visibility = View.VISIBLE
            delete_state.visibility = View.VISIBLE
        }
        else{
            updeite_state.visibility = View.GONE
            RG_order_updiet.visibility = View.GONE
            delete_state.visibility = View.GONE
            btn_call_the_costmer.visibility = View.GONE
        }

    }

    private fun showInfo(order: Orders) {
        costmor_name.text = order.info.get("name").toString()
        cost_phone.text = order.info.get("phone").toString()
        cost_contry.text = order.info.get("contry").toString()
        cost_adress.text = order.info.get("fulladdress").toString()
        cost_city.text = order.info.get("city").toString()
        costmor_pay_way.text = order.info.get("pay").toString()
        cost_total_price.text = order.info.get("totalprice").toString()

        if (order.info.containsKey("monyState")){
            costmor_monyState.text = order.info.get("monyState").toString()
        }

        btn_call_the_costmer.setOnClickListener {
            copyCorimy(order.info.get("phone").toString())
            goWhatsapp(order.info.get("phone").toString())
        }


    }
    private fun copyCorimy(textToCopy:String){
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = ClipData.newPlainText("Label", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this,"تم نسخ رقم العميل",Toast.LENGTH_SHORT).show()


    }

    private fun goWhatsapp(phoneNumber:String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phoneNumber"))

        // الحصول على قائمة بجميع التطبيقات المثبتة التي يمكن استخدامها لفتح النية
        val packageManager = packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        val isIntentSafe = activities.isNotEmpty()

        if (isIntentSafe) {
            // عرض خيارات للمستخدم لتحديد التطبيق الذي يناسبه لفتح النية
            val chooser = Intent.createChooser(intent, "Open with")
            startActivity(chooser)
        } else {
            // في حالة عدم توفر تطبيقات WhatsApp على جهاز المستخدم، يتم عرض رسالة خطأ للمستخدم
            Toast.makeText(
                this,
                "WhatsApp is not installed on your device",
                Toast.LENGTH_SHORT
            ).show()
        }
    }



    private fun imelmnt() {
        cost_city = findViewById(R.id.cost_city)
        cost_phone = findViewById(R.id.cost_phone)
        cost_contry = findViewById(R.id.cost_contry)
        cost_adress = findViewById(R.id.cost_adress)
        deel_done_rb = findViewById(R.id.deel_done_rb)
        delete_state = findViewById(R.id.delete_state)
        costmor_name = findViewById(R.id.costmor_name)
        updeite_state = findViewById(R.id.updeite_state)
        back_orderDitel = findViewById(R.id.back_orderDitel)
        RG_order_updiet = findViewById(R.id.RG_order_updiet)
        ad_new_order_rb = findViewById(R.id.ad_new_order_rb)
        costmor_pay_way = findViewById(R.id.costmor_pay_way)
        prog_order_detel = findViewById(R.id.prog_order_detel)
        cost_total_price = findViewById(R.id.cost_total_price)
        btn_cansel_order = findViewById(R.id.btn_cansel_order)
        list_order_detels = findViewById(R.id.list_order_detels)
        btn_call_the_costmer = findViewById(R.id.btn_call_the_costmer)

        costmor_monyState = findViewById(R.id.costmor_monyState)
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_delete_conferm)

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)



        prog_delete = dialog.findViewById(R.id.prog_delete)
		btn_delete_cansel = dialog.findViewById(R.id.btn_delete_cansel)
		btn_delete_conferm = dialog.findViewById(R.id.btn_delete_conferm)

        btn_bils_order_out = findViewById(R.id.btn_bils_order_out)


    }
}