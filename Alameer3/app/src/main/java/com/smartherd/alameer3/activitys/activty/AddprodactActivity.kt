package com.smartherd.alameer3.activitys.activty

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.net.toUri

import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.adapders.CustomAdapterList
import com.smartherd.alameer3.activitys.models.Prodacts
import com.smartherd.alameer3.activitys.models.Section
import com.theartofdev.edmodo.cropper.CropImage

import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddprodactActivity : AppCompatActivity() {
    private lateinit var chick_one_much: CheckBox

    private lateinit var chick_one_price: CheckBox
    private lateinit var chick_one_color: CheckBox

    private lateinit var chick_one_decount: CheckBox


    private lateinit var post_now: Button

    private lateinit var chos_sction: Spinner
    private lateinit var btn_add_price: Button
    private lateinit var input_color: AutoCompleteTextView

    private lateinit var summary_size: EditText
    //private lateinit var add_prod_name: EditText
    private lateinit var input_decsont: EditText
    private lateinit var add_prod_about: AutoCompleteTextView
    private lateinit var add_prod_price: EditText
    private lateinit var btn_add_descount: Button
    private lateinit var chos_size_color: Spinner
    private lateinit var add_prod_imeg_btn: Button
    private lateinit var prod_Addprod: ProgressBar
    private lateinit var chos_size_descount: Spinner
    private lateinit var chos_size_count: Spinner
    private lateinit var summary_count:EditText
    private lateinit var summary_descount:EditText

    private lateinit var summary_colors:EditText

    private lateinit var input_size_prodact: AutoCompleteTextView
    private lateinit var add_prod_count:AutoCompleteTextView
    private lateinit var btn_add_count:Button
    private lateinit var add_prod_img_show: ImageView
    private lateinit var btn_save_color:Button

    private lateinit var chick_price_down:CheckBox
    private lateinit var add_prod_spti_price:EditText

    private lateinit var add_prod_name:AutoCompleteTextView
    private lateinit var show_prod_imeg_btn:Button
    private lateinit var dialog: Dialog

    private lateinit var btn_prev:Button
    private lateinit var btn_next:Button
    private lateinit var dialog2: Dialog
    private lateinit var show_name_pr:TextView
    private lateinit var show_about_pr:TextView

    private lateinit var datePicker_desc1:DatePicker
    private lateinit var chick_discount_time1:CheckBox

    var flag = false

    var cardIndex = 0
    var listCards = ArrayList<CardView>()


    private var sectionSelect = ""
    var name = ""
    var disk = ""

    private var sizes = ArrayList<String>()
    private var descount_sizes = LinkedHashMap<String,String>()
    private var count_sizes = LinkedHashMap<String,String>()
    private lateinit var selected_img:Spinner
    private var colors_size = LinkedHashMap<String,ArrayList<String>>()
    private var dowon_price = LinkedHashMap<String,String>()
    private lateinit var btn_show_all_samary:Button

    var flag2 = -1
    private lateinit var list_img_n:GridView
    var str = kotlin.collections.ArrayList<String>()
    var count = 0
    var imgePostion = 0
    var select=0
    var datadiscount:Date? = null
    var img_size:HashMap<String,String> = HashMap<String,String>()
    var img_color:HashMap<String,String> = HashMap<String,String>()

    var sizeNewArry = ArrayList<HashMap<String,String>>()

    private lateinit var compressedImageUri: Uri

    private lateinit var imeg:ArrayList<Uri>

    var loclImag = ArrayList<Uri>()
    private lateinit var btn_imeg_order:Button
    private lateinit var btn_reorder_zero_img:Button
    private lateinit var btn_delete_img:Button

    private lateinit var dialog3: Dialog
    private lateinit var delet_text: TextView
    private lateinit var prog_delete: ProgressBar
    private lateinit var btn_delete_cansel: android.widget.Button
    private lateinit var btn_delete_conferm: android.widget.Button

    private lateinit var updata_add_img_size:CardView
    private lateinit var btn_img_size:Button
    private lateinit var updata_add_img_color:CardView
    private lateinit var btn_img_color:Button
    private lateinit var dialog4:Dialog



    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){ uri->


        if(uri !=null) {

            GlobalScope.launch(Dispatchers.Main) {
                uri.forEach {tt->
                    val compressedImageFile = compress(this@AddprodactActivity,tt)
                    compressedImageUri = Uri.fromFile(compressedImageFile)
                    loclImag.add(compressedImageUri)
                    str.add((count++).toString())
                }

                select = loclImag.size - 1
                btn_imeg_order.visibility = View.VISIBLE



            }


        }









    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addprodact)





        imelmnt()
        getSections()
        imeg=ArrayList<Uri>()
        sizes=ArrayList<String>()

        btn_add_price.setOnClickListener {
            addSizeAndPrices()
        }

        btn_add_descount.setOnClickListener {
            setDescounts()
        }



        btn_prev.setOnClickListener {
            if(cardIndex >0){
                listCards[cardIndex].visibility = View.GONE
                cardIndex--
                listCards[cardIndex].visibility = View.VISIBLE

            }
        }

        btn_next.setOnClickListener {

            when(cardIndex){

                0 ->{
                    if(imeg.size>0 && add_prod_name.text.toString().isNotEmpty() && add_prod_about.text.toString().isNotEmpty()){
                        name = add_prod_name.text.toString()
                        disk = add_prod_about.text.toString()
                        show_name_pr.text = name
                        show_about_pr.text = disk
                        btn_show_all_samary.visibility = View.VISIBLE
                        listCards[cardIndex].visibility = View.GONE
                        cardIndex++
                        listCards[cardIndex].visibility = View.VISIBLE
                        btn_reorder_zero_img.visibility = View.GONE

                    }
                    else{
                        Snackbar.make(listCards[cardIndex],"هناك حقل ناقص او لا يوجد صور",Toast.LENGTH_SHORT).show()

                    }
                }

                1->{
                    if(sizeNewArry.size>0){

                            listCards[cardIndex].visibility = View.GONE
                            cardIndex++
                            listCards[cardIndex].visibility = View.VISIBLE


                        }
                    else{
                        Snackbar.make(listCards[cardIndex],"تأكد من حفظ المقاسات والاسعار اولاً !",Snackbar.LENGTH_SHORT).show()

                    }
                }
                2->{
                    listCards[cardIndex].visibility = View.GONE
                    cardIndex++
                    listCards[cardIndex].visibility = View.VISIBLE
                }
                3->{

                    listCards[cardIndex].visibility = View.GONE
                    cardIndex++
                    listCards[cardIndex].visibility = View.VISIBLE

                }
                4->{
                    if(!count_sizes.isEmpty()){
                        if(count_sizes[count_sizes.keys.first()]!=null || count_sizes[count_sizes.keys.first()].toString().isNotEmpty()){
                            listCards[cardIndex].visibility = View.GONE
                            cardIndex++
                            listCards[cardIndex].visibility = View.VISIBLE
                        }
                        else{
                            Snackbar.make(listCards[cardIndex],"تأكد من حفظ  الكميات !",Snackbar.LENGTH_SHORT).show()

                        }
                    }else{
                        Snackbar.make(listCards[cardIndex],"تأكد من حفظ  الكيمات اولاً !",Snackbar.LENGTH_SHORT).show()

                    }
                }
                5->{

                    if(colors_size.isEmpty()){
                        Snackbar.make(listCards[cardIndex],"ملاحظة / لم يتم اضافة اللوان !",Snackbar.LENGTH_SHORT).show()

                        listCards[cardIndex].visibility = View.GONE
                        cardIndex++
                        listCards[cardIndex].visibility = View.VISIBLE
                    }
                    else{
                        listCards[cardIndex].visibility = View.GONE
                        cardIndex++
                        listCards[cardIndex].visibility = View.VISIBLE
                    }

                }
                6->{

                    listCards[cardIndex].visibility = View.GONE
                    cardIndex++
                    listCards[cardIndex].visibility = View.VISIBLE
                }
                7->{
                    post_now.visibility = View.VISIBLE
                }


            }

        }

        btn_show_all_samary.setOnClickListener {
            dialog2.show()
        }



        chick_price_down.setOnCheckedChangeListener { buttonView, isChecked ->

            if(chick_price_down.isChecked){
                add_prod_spti_price.visibility = View.VISIBLE
            }
            else{
                add_prod_spti_price.visibility = View.GONE
            }

        }

        chick_discount_time1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                datePicker_desc1.visibility = View.VISIBLE
            }
            else{
                datePicker_desc1.visibility = View.GONE
            }
        }




        btn_save_color.setOnClickListener {
            seveColors()

        }
        btn_add_count.setOnClickListener {

            addCounts()


        }

        add_prod_imeg_btn.setOnClickListener {
            getContent.launch("image/*")
        }
        post_now.setOnClickListener {
            uplodeProdact(imeg)
        }



        show_prod_imeg_btn.setOnClickListener {
            if(loclImag.size>0 && imeg.size >0){
                if(imeg.size == loclImag.size){
                    flag2 = 1
                    var listImageAd = CustomAdapterList(this,imeg)
                    list_img_n.adapter = listImageAd
                    dialog.show()
                }
                else{
                    Toast.makeText(this,"لم يتم ترتيب جميع الصور",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"لا يوجد صور",Toast.LENGTH_SHORT).show()
            }


        }

        list_img_n.setOnItemClickListener { parent, view, position, id ->

            if(flag2 == 0){

                imeg.add(loclImag[position])
                Toast.makeText(this,"ترتيب ( "+(imeg.size).toString()+"(",Toast.LENGTH_SHORT).show()
                if (imeg.size == loclImag.size){
                    dialog.cancel()
                    Snackbar.make(btn_imeg_order,"تم ترتيب الصور بنجاح",Snackbar.LENGTH_SHORT).show()
                    show_prod_imeg_btn.visibility = View.VISIBLE
                    btn_delete_img.visibility = View.VISIBLE
                    btn_reorder_zero_img.visibility = View.VISIBLE
                    btn_imeg_order.visibility = View.GONE
                }
            }
            else if (flag2 ==1){
                val imageUri =imeg[position]
                select = position
                CropImage.activity(imageUri).start(this)
            }
            else if(flag2 == 2){

                dialog3.show()

                btn_delete_conferm.setOnClickListener {
                    prog_delete.visibility = View.VISIBLE
                    imeg.removeAt(position)
                    prog_delete.visibility = View.GONE
                    dialog3.cancel()
                    dialog.cancel()
                    Toast.makeText(this,"تم الحذف بنجاح",Toast.LENGTH_SHORT).show()
                }


            }
            else if(flag2 == 3){
                var imgUri = position.toString()


                val tital_dialog =dialog4.findViewById<TextView>(R.id.tital_dialog);
                val choos_color_size_img = dialog4.findViewById<Spinner>(R.id.choos_color_size_img);
                val btn_finsh_connect_img = dialog4.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_finsh_connect_img);
                val btn_cansel_connect_img = dialog4.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_cansel_connect_img);

                tital_dialog.text = "اختار مقاس لربطة مع الصورة"
                choos_color_size_img.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes)

                dialog4.show()

                btn_finsh_connect_img.setOnClickListener {
                    img_size.put(choos_color_size_img.selectedItem.toString(),imgUri)
                    Toast.makeText(this,"تم الربط !",Toast.LENGTH_SHORT).show()
                    dialog4.cancel()
                }
            }
            else if(flag2 == 4){

                var imgUri = position.toString()

                var mm = colors_size.values.firstOrNull()
                if(mm!=null && !mm.isEmpty()){
                    val tital_dialog = dialog4.findViewById<TextView>(R.id.tital_dialog);
                    val choos_color_size_img = dialog4.findViewById<Spinner>(R.id.choos_color_size_img);
                    val btn_finsh_connect_img =
                        dialog4.findViewById<AppCompatButton>(R.id.btn_finsh_connect_img);
                    val btn_cansel_connect_img =
                        dialog4.findViewById<AppCompatButton>(R.id.btn_cansel_connect_img);
                    tital_dialog.text = "اختر لون لربطة مع الصورة"

                    choos_color_size_img.adapter =
                        ArrayAdapter(this, android.R.layout.simple_spinner_item,mm)
                    dialog4.show()

                    btn_finsh_connect_img.setOnClickListener {
                        img_color.put(choos_color_size_img.selectedItem.toString(), imgUri)
                        Toast.makeText(this, "تم الربط !", Toast.LENGTH_SHORT).show()
                        dialog4.cancel()
                    }
                }
                else{
                    Snackbar.make(btn_img_color,"لا يوجد اللوان",Snackbar.LENGTH_SHORT).show()
                }

            }





        }

        btn_reorder_zero_img.setOnClickListener {
            imeg.clear()
            Snackbar.make(btn_imeg_order,"تم تصفير الترتيب يمكنك الاختيار والترتيب من جديد",Snackbar.LENGTH_SHORT).show()
            btn_imeg_order.visibility = View.VISIBLE
        }

        btn_imeg_order.setOnClickListener {

            if(loclImag.size>0){
                flag2 = 0
                var listImageAd = CustomAdapterList(this,loclImag)
                list_img_n.adapter = listImageAd
                dialog.show()

            }

        }

        btn_img_size.setOnClickListener {
            flag2 = 3
            var listImageAd = CustomAdapterList(this,imeg)
            list_img_n.adapter = listImageAd
            dialog.show()
        }
        btn_img_color.setOnClickListener {
            flag2 = 4
            var listImageAd = CustomAdapterList(this,imeg)
            list_img_n.adapter = listImageAd
            dialog.show()
        }

        btn_delete_img.setOnClickListener {
            if (imeg.size>0){
                flag2 = 2
                var listImageAd = CustomAdapterList(this,loclImag)
                list_img_n.adapter = listImageAd
                dialog.show()
            }
        }
//
    }

    fun splitText(text: String): ArrayList<String> {
        val words = text.split("\n") // تقسيم النص إلى كلمات باستخدام الفاصلة,"
        return ArrayList(words) // تحويل القائمة إلى مصفوفة ArrayList وإرجاعها
    }

    private fun uplodeProdact( images:ArrayList<Uri>){
        name = add_prod_name.text.toString()
         disk = add_prod_about.text.toString()
        prod_Addprod.visibility = View.VISIBLE
        var firestore = FirebaseFirestore.getInstance()
        var firebaseStorage = FirebaseStorage.getInstance()

        val newpostRef = firestore.collection("prodacts2").document()
        val urls =ArrayList<String>()


        val uploadTasks = images.map { imageUri ->
            val ref = firebaseStorage.getReference().child("photo2")
                .child(newpostRef.id)
                .child(UUID.randomUUID().toString())

            ref.putFile(imageUri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    ref.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result.toString()
                        urls.add(downloadUri)

                        if (urls.size == images.size) {
                            val prodact = Prodacts(name, disk, Calendar.getInstance().time ,Calendar.getInstance().time,sectionSelect,sizeNewArry,urls,colors_size ,count_sizes,descount_sizes,"1",dowon_price,datadiscount,img_size,img_color)
                            firestore.collection("prodacts2").document(newpostRef.id).set(prodact)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        prod_Addprod.visibility = View.GONE
                                        Toast.makeText(this, "تمت الاضافة بنجاح!", Toast.LENGTH_SHORT)
                                            .show()
                                        finish()
                                    }
                                }
                        }
                    }
                }
        }

        Tasks.whenAllComplete(uploadTasks)






    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri

                imeg[select] = resultUri
                Toast.makeText(this@AddprodactActivity,imeg.size.toString(),Toast.LENGTH_SHORT).show()

                Toast.makeText(this,"done !",Toast.LENGTH_SHORT)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this,"errer !",Toast.LENGTH_SHORT)

            }
        }
    }


    private fun addCounts() {
        if (chos_size_count.selectedItem != null && add_prod_count.text.toString().isNotEmpty()) {
            var count = add_prod_count.text.toString()
            if(chick_one_much.isChecked){
                sizeNewArry.forEach { aa->
                    aa.keys.forEach {ss->
                        count_sizes.put(ss, count)
                    }

                }
            }

            else{
                var sizecount = chos_size_count.selectedItem.toString()
                count_sizes.put(sizecount, count)
            }


            Toast.makeText(this, "تمت اضافة الكمية للمقاس", Toast.LENGTH_SHORT).show()
            add_prod_count.text.clear()

        }
        getSummry()
    }

    private fun seveColors() {
        if (chos_size_color.selectedItem != null && input_color.text.toString().isNotEmpty()) {

            var colorsText = input_color.text.toString()

            var colorList = splitText(colorsText)

            if(chick_one_color.isChecked){
                sizeNewArry.forEach {aa->
                    aa.keys.forEach {ss->
                        colors_size.put(ss,colorList)

                    }


                }

            }
            else{
                var selectSize = chos_size_color.selectedItem.toString()
                colors_size.put(selectSize,colorList)
                colorList.forEach {cc->
                }

            }

            Toast.makeText(this,"تمت اضافة الالوان بنجاح",Toast.LENGTH_SHORT).show()
            input_color.text.clear()


        }
        getSummry()
    }


    private fun setDescounts() {
        if (chos_size_descount.selectedItem != null && input_decsont.text.toString().isNotEmpty()) {

            if(chick_discount_time1.isChecked) {
                val year = datePicker_desc1.year
                val month = datePicker_desc1.month
                val day = datePicker_desc1.dayOfMonth
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)
                val fullDate = calendar.time
                datadiscount = fullDate

            }

                var desc = input_decsont.text.toString()
                summary_descount.text.clear()
                // set one decount for all sizes
                if(chick_one_decount.isChecked){
                    sizeNewArry.forEach {aa->
                        aa.keys.forEach {ss->
                            descount_sizes.put(ss, desc)


                        }

                    }
                }
                else{
                    var sizes = chos_size_descount.selectedItem.toString()
                    descount_sizes.put(sizes, desc)
                }








            input_decsont.text.clear()
            Toast.makeText(this,"تمت اضافة التخفيض",Toast.LENGTH_SHORT).show()


        }

        getSummry()
    }

    private fun addSizeAndPrices() {

        if (input_size_prodact.text.toString().isNotEmpty() && add_prod_price.text.toString()
                .isNotEmpty()) {
            var size = input_size_prodact.text.toString()
            var price = add_prod_price.text.toString()

            summary_size.text.clear()

            if(chick_one_price.isChecked){
                var sizeList = splitText(size)




                if(chick_price_down.isChecked){
                    if(add_prod_spti_price.text.toString().isNotEmpty()){
                      var  sp = add_prod_spti_price.text.toString()

                        sizeList.forEach { sss->
                            dowon_price.put(sss,sp)
                        }

                    }
                    sizeList.forEach {ss->
                        sizeNewArry.add(hashMapOf(ss to price))
                        sizes.add(ss)
                    }

                }
                else{
                    sizeList.forEach { ss->
                        sizeNewArry.add(hashMapOf(ss to price))
                        sizes.add(ss)

                    }
                }


            }
            else{
                if(chick_price_down.isChecked){
                    if(add_prod_spti_price.text.toString().isNotEmpty()) {
                      var  sp = add_prod_spti_price.text.toString()
                        dowon_price.put(size,sp)

                    }
                    }
                sizeNewArry.add(hashMapOf(size to price))
                sizes.add(size)
            }

            chos_size_descount.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes)
            chos_size_color.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes)
            chos_size_count.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes)

            input_size_prodact.text.clear()
            add_prod_price.text.clear()
            add_prod_spti_price.text.clear()

        }

        getSummry()
    }



    private fun getSummry(){


        if(sizeNewArry.size>0){
            summary_size.text.clear()
            sizeNewArry.forEach { aa->
                aa.keys.forEach {ss->
                    summary_size.setText(summary_size.text.toString()+ss+" = " +aa[ss].toString()+"\n")


                }
            }
            if(!dowon_price.isEmpty()){
                summary_size.setText("\n"+summary_size.text.toString()+"اسعار للجنوب : "+"\n")
                dowon_price.keys.forEach {ss->
                    summary_size.setText(summary_size.text.toString()+ss+" = " +dowon_price[ss].toString()+"\n")
                }

            }
        }






        if(!colors_size.isEmpty()){
            summary_colors.text.clear()

            colors_size.keys.forEach {si->
                summary_colors.setText(summary_colors.text.toString() + "\n"+ si + "=")
                colors_size[si]!!.forEach { colo->
                    summary_colors.setText(summary_colors.text.toString() + " , "+ colo + " ")
                }

            }
        }

        if(!descount_sizes.isEmpty()){
            summary_descount.text.clear()
            descount_sizes.keys.forEach {so->
                summary_descount.setText(summary_descount.text.toString() + "\n"+ so +" = " +descount_sizes[so].toString())
            }
        }




        if (!count_sizes.isEmpty()){
            summary_count.text.clear()

            count_sizes.keys.forEach {ss->
                summary_count.setText(summary_count.text.toString() +"\n" + ss+" = "+count_sizes[ss].toString())

            }
        }



    }


    private fun getSections() {
        var sectionName = ArrayList<String>()
        var sectionid = ArrayList<String>()

        var firestore = FirebaseFirestore.getInstance()

        var dp = firestore.collection("sections")

        dp.get().addOnSuccessListener {
            it.documents.forEach { posts ->
                sectionName.add(posts.get("name").toString())
                sectionid.add(posts.id)

            }
            var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sectionName)

            chos_sction.adapter = adapter
            chos_sction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sectionSelect = sectionid[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }





        }
    }

    fun imelmnt() {


        add_prod_name = findViewById(R.id.add_prod_name)

        post_now = findViewById(R.id.post_now)
        input_color = findViewById(R.id.input_color)
        chos_sction = findViewById(R.id.chos_sction)

        prod_Addprod = findViewById(R.id.prod_Addprod)

        btn_add_price = findViewById(R.id.btn_add_price)
        input_decsont = findViewById(R.id.input_decsont)

        add_prod_about = findViewById(R.id.add_prod_about)
        add_prod_price = findViewById(R.id.add_prod_price)
        chos_size_color = findViewById(R.id.chos_size_color)
        btn_add_descount = findViewById(R.id.btn_add_descount)
        add_prod_imeg_btn = findViewById(R.id.add_prod_imeg_btn)

        input_size_prodact = findViewById(R.id.input_size_prodact)
        chos_size_descount = findViewById(R.id.chos_size_descount)
        btn_save_color = findViewById(R.id.btn_save_color)

        btn_add_count= findViewById(R.id.btn_add_count)
        add_prod_count = findViewById(R.id.add_prod_count)
        chos_size_count = findViewById(R.id.chos_size_count)


        chick_one_much = findViewById(R.id.chick_one_much)

        chick_one_price = findViewById(R.id.chick_one_price)

        chick_one_color = findViewById(R.id.chick_one_color)

        chick_one_decount = findViewById(R.id.chick_one_decount)


         chick_price_down = findViewById(R.id.chick_price_down)
         add_prod_spti_price = findViewById(R.id.add_prod_spti_price)

        show_prod_imeg_btn = findViewById(R.id.show_prod_imeg_btn)

        chick_discount_time1 = findViewById(R.id.chick_discount_time1)
        datePicker_desc1 = findViewById(R.id.datePicker_desc1)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_list_img)


        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

        list_img_n =dialog.findViewById(R.id.list_img_n)


        btn_prev = findViewById(R.id.btn_prev)
        btn_next = findViewById(R.id.btn_next)


        listCards.add(findViewById(R.id.card_1))
        listCards.add(findViewById(R.id.card_2))
        listCards.add(findViewById(R.id.updata_add_img_size1))
        listCards.add(findViewById(R.id.card_3))
        listCards.add(findViewById(R.id.card_4))
        listCards.add(findViewById(R.id.card_5))
        listCards.add(findViewById(R.id.updata_add_img_color1))
        listCards.add(findViewById(R.id.card_6))



        btn_show_all_samary = findViewById(R.id.btn_show_all_samary)

        dialog2 = Dialog(this)
        dialog2.setContentView(R.layout.dialog_all_summary)

        dialog2.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog2.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.window?.setGravity(Gravity.BOTTOM)

        show_about_pr = dialog2.findViewById(R.id.show_about_prod)
        show_name_pr = dialog2.findViewById(R.id.show_name_pr)

        summary_descount = dialog2.findViewById(R.id.summary_descount)
        summary_size = dialog2.findViewById(R.id.summary_size)

        summary_colors = dialog2.findViewById(R.id.summary_colors)
        summary_count = dialog2.findViewById(R.id.summary_count)

        btn_imeg_order = findViewById(R.id.btn_imeg_order)


        dialog3 = Dialog(this)
        dialog3.setContentView(R.layout.dialog_delete_conferm)



        btn_img_size = findViewById(R.id.btn_img_size1)

        btn_img_color = findViewById(R.id.btn_img_color1)


        dialog4 = Dialog(this)
        dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog4.setContentView(R.layout.dialog_img_color_size)



        dialog4.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog4.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog4.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog4.window?.setGravity(Gravity.CENTER)





        dialog3.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog3.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog3.window?.setGravity(Gravity.BOTTOM)


        btn_delete_cansel = dialog3.findViewById(R.id.btn_delete_cansel)
        btn_delete_conferm = dialog3.findViewById(R.id.btn_delete_conferm)
        prog_delete = dialog3.findViewById(R.id.prog_delete)
        delet_text = dialog3.findViewById(R.id.delet_text)


        btn_reorder_zero_img = findViewById(R.id.btn_reorder_zero_img)
     btn_delete_img = findViewById(R.id.btn_delete_img)
    }
    suspend fun compress(context: Context, uri: Uri): File {
        return withContext(Dispatchers.IO) {
            Compressor.compress(context, FileUtil.from(context,uri)) {
                default()
            }
        }
    }

    object FileUtil {
        fun from(context: Context, uri: Uri): File {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("image", null, context.cacheDir)
            tempFile.deleteOnExit()
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return tempFile
        }
    }



    override fun onBackPressed() {
        if(flag){

            super.onBackPressed()
        }
        else{
            Toast.makeText(this,"اضغط مرة اخرى للخروج من التطبيق",Toast.LENGTH_SHORT).show()
            flag = true
        }

    }


    var firestore = FirebaseFirestore.getInstance()
//        var prodName = ArrayList<String>()
//        var prodAbout = ArrayList<String>()
//        var prodSizes = ArrayList<String>()
//        var  prodColors= ArrayList<String>()
//       var pro= firestore.collection("prodacts2")
//        pro.get().addOnSuccessListener { dd->
//            dd.documents.forEach { doc->
//                val prodItem = doc.toObject(Prodacts::class.java)!!
//                prodName.add(prodItem.productName)
//                prodAbout.add(prodItem.about)
//                prodItem.colors.keys.forEach {ss->
//                    prodColors.add(prodItem.colors[ss]!![0])
//
//                }
//                prodItem.sizes.keys.forEach {si->
//                    prodSizes.add(si.toString())
//                }
//
//            }
//
//        }
//
//        val adapterName = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, prodName)
//        add_prod_name.setAdapter(adapterName)
//
//        val adapterAbout = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, prodAbout)
//        add_prod_about.setAdapter(adapterAbout)
//
//        val adapterSizes = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, prodSizes)
//
//        input_size_prodact.setAdapter(adapterSizes)
//
//
//        val adapterColors = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, prodColors)
//
//        input_color.setAdapter(adapterColors)
//
//
//        val adapterCount = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayListOf("10","15","20","30","40"))
//
//        add_prod_count.setAdapter(adapterCount)


}