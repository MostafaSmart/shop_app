package com.smartherd.alameer3.activitys.activty

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
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.adapders.CustomAdapterList
import com.smartherd.alameer3.activitys.models.Banals
import com.smartherd.alameer3.activitys.models.Prodacts
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BanalsCingActivity : AppCompatActivity() {
    private lateinit var banal_count: TextView
    private lateinit var heder_ban: LinearLayout
    private lateinit var add_photo_banal: Button
    private lateinit var post_photo_banal: Button
    private lateinit var prog_post_banals: ProgressBar

    private lateinit var heder_bake: ImageView
    private lateinit var hedar_tital: TextView
    private lateinit var compressedImageUri:Uri
    private lateinit var imeg:ArrayList<Uri>


    private lateinit var banal_imag: Spinner
    private lateinit var banal_prod: Spinner


    var totalCount = ArrayList<String>()






    private lateinit var chick_panal_prodact: CheckBox

    private lateinit var connect_banal_prodact: LinearLayout
    private lateinit var btn_edit_panal: android.widget.Button


    private lateinit var btn_conect_b_p: android.widget.Button
    private lateinit var vis_mult_panal:LinearLayout


    private lateinit var dialog: Dialog
    private lateinit var list_img_n:GridView

    private lateinit var dialog2: Dialog
    private lateinit var list_img_n2:GridView


    private lateinit var btn_add_one_img:Button
    private lateinit var img_show:ImageView

    private lateinit var chick_pan_only:CheckBox
    private lateinit var chick_delete_pan:CheckBox
    private lateinit var btn_delete_list_banal:Button


    private lateinit var dialog3: Dialog
    private lateinit var prog_delete: ProgressBar
    private lateinit var btn_delete_cansel: Button
    private lateinit var btn_delete_conferm: Button


    var count = -1

    var imgPos = 0
    var selectProd = -1
    var selectImg= -1
    var oneImg= HashMap<String,String>()
    var flag = -1
    private val getContent =  registerForActivityResult(ActivityResultContracts.GetMultipleContents()){ uri->

        if(uri!=null) {
            GlobalScope.launch(Dispatchers.Main) {

                uri.forEach {yy->
                    val compressedImageFile = compress(this@BanalsCingActivity, yy!!)
                    // تحويل ملف الصورة المضغوطة إلى URI
                    compressedImageUri = Uri.fromFile(compressedImageFile)

                    imeg.add(compressedImageUri)
                    count++
                    totalCount.add(count.toString())

                }

                banal_count.text = imeg.size.toString()

            }
        }
        else{
            Toast.makeText(this,"فشل في تحميل الصورة قم باختيار صورة مرة اخرى",Toast.LENGTH_LONG).show()
        }



    }


    private val getContent1 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if(uri!=null) {
            GlobalScope.launch(Dispatchers.Main) {
                val compressedImageFile = compress(this@BanalsCingActivity, uri)
                compressedImageUri = Uri.fromFile(compressedImageFile)
                imeg.add(compressedImageUri)
                oneImg.put("name","nun")
                oneImg.put("img",compressedImageUri.toString())
                img_show.visibility = View.VISIBLE
                img_show.setImageURI(compressedImageUri)
                CropImage.activity(compressedImageUri).start(this@BanalsCingActivity)



            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banals_cing)





        imelmnt()

        headerSteup()

        onClickLissins()



        getProdactAndDesplay()

        chickBoxLisenner()



        btn_add_one_img.setOnClickListener {
            if(imeg.size < 1){
                flag = 1
                getContent1.launch("image/*")
            }
            else{
                Snackbar.make(btn_add_one_img,"لا يمكن اضافة اكثر من صورة",Snackbar.LENGTH_SHORT).show()
            }
        }

        btn_edit_panal.setOnClickListener {
            if(imeg.size>0){
                var listImageAd = CustomAdapterList(this,imeg)
                list_img_n.adapter = listImageAd
                dialog.show()
            }
        }

        list_img_n.setOnItemClickListener { parent, view, position, id ->
            imgPos = position
            val imageUri =imeg[position]

            CropImage.activity(imageUri).start(this)

        }






    }




    private fun getProdactAndDesplay() {
        var dataList = ArrayList<String>()
        var dataIdList = ArrayList<String>()
        var firestore = FirebaseFirestore.getInstance()
        firestore.collection("prodacts2").orderBy("productName").get().addOnSuccessListener { ref ->
            ref.documents.forEach { prod ->
                val dd = prod.toObject(Prodacts::class.java)!!
                dataList.add(dd.productName)
                dataIdList.add(prod.id)
            }
            banal_prod.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dataList)



            btn_conect_b_p.setOnClickListener {

                if(banal_prod.selectedItem!=null && imeg.size == 1){

                    var mm = hashMapOf<String,String>("name" to dataIdList[selectProd]
                        , "img" to imeg[0].toString())

                    upludOneWithProd(mm)
                }


            }

            post_photo_banal.setOnClickListener {
                if (imeg.size > 0) {
                    BanalsUploud(imeg)
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                if(flag == 1){

                    imeg[0] = resultUri
                    oneImg.put("img",resultUri.toString())
                    img_show.setImageURI(resultUri)

                }
                else{

                    imeg[imgPos] = resultUri

                    Toast.makeText(this@BanalsCingActivity,imeg.size.toString(),Toast.LENGTH_SHORT).show()

                    banal_imag.adapter  = ArrayAdapter(this@BanalsCingActivity, android.R.layout.simple_spinner_item,totalCount)

                }






                Toast.makeText(this,"done !",Toast.LENGTH_SHORT)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this,"errer !",Toast.LENGTH_SHORT)

            }
        }
    }

    private fun onClickLissins() {
        add_photo_banal.setOnClickListener {
            flag = 0
            getContent.launch("image/*")

        }


        chick_panal_prodact.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                connect_banal_prodact.visibility = View.VISIBLE
            }
            else{
                connect_banal_prodact.visibility = View.GONE

            }
        }

        banal_prod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,id: Long
            ) {
                selectProd = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }

        banal_imag.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectImg = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }


    }


    private fun chickBoxLisenner(){
        chick_panal_prodact.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                connect_banal_prodact.visibility = View.VISIBLE
                chick_pan_only.isChecked = false
                chick_delete_pan.isChecked = false
                imeg.clear()
            }
            else{
                connect_banal_prodact.visibility = View.GONE


            }
        }

        chick_pan_only.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                vis_mult_panal.visibility = View.VISIBLE
                chick_panal_prodact.isChecked = false
                chick_delete_pan.isChecked = false
                imeg.clear()
            }
            else{
                vis_mult_panal.visibility = View.GONE

            }
        }


        chick_delete_pan.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btn_delete_list_banal.visibility = View.VISIBLE
                chick_panal_prodact.isChecked = false

                chick_pan_only.isChecked = false
                delet()
            }
            else{
                btn_delete_list_banal.visibility = View.GONE

            }
        }



    }

    private fun upludOneWithProd(newBanal:HashMap<String,String>){
        val newpostRef = "n4gMxzXo88sJFcEdHo1k"
        var fires = FirebaseFirestore.getInstance().collection("panals").document(newpostRef)

        prog_post_banals.visibility = View.VISIBLE
        val ref = FirebaseStorage.getInstance().getReference().child("photo")
            .child(newpostRef)
            .child(UUID.randomUUID().toString())

        ref.putFile(imeg[0]).addOnSuccessListener {

            ref.downloadUrl.addOnCompleteListener {uur->
                if(uur.isSuccessful){
                    newBanal["img"] = uur.result.toString()
                    fires.get().addOnSuccessListener {panal->
                        val bb = panal.toObject(Banals::class.java)!!
                        bb.imgurl.add(newBanal)

                        fires.set(bb).addOnSuccessListener {
                            Toast.makeText(this,"تم الرفع بنجاح ",Toast.LENGTH_SHORT).show()
                            prog_post_banals.visibility =View.GONE
                        }

                    }

                }


            }


        }

    }

    fun splitText2(text: String): ArrayList<String> {
        val words = text.split("/") // تقسيم النص إلى كلمات باستخدام الفاصلة,"
        return ArrayList(words) // تحويل القائمة إلى مصفوفة ArrayList وإرجاعها
    }

    private fun delet(){

        var newRef= "n4gMxzXo88sJFcEdHo1k"
        var fire = FirebaseFirestore.getInstance().collection("panals").document(newRef)

        fire.get().addOnSuccessListener { ban->
            var dataP = ban.toObject(Banals::class.java)!!
            var names = ArrayList<String>()
            var imguu = ArrayList<Uri>()
            var index = -1
            dataP.imgurl.forEach {
                imguu.add(Uri.parse(it["img"]))
                names.add(it["name"]!!)
            }

            btn_delete_list_banal.setOnClickListener {
                var listImageAd = CustomAdapterList(this,imguu)
                list_img_n2.adapter = listImageAd
                dialog2.show()
            }

            list_img_n2.setOnItemClickListener { parent, view, position, id ->
                index = position
                val fullPath = imguu[position].lastPathSegment
                val photoFullName = splitText2(fullPath.toString())
                val name = photoFullName[2]
                dialog3.show()
                btn_delete_conferm.setOnClickListener {
                    prog_delete.visibility = view.visibility
                    var imegsfolder = FirebaseStorage.getInstance().getReference().child("photo").child(newRef)
                        imegsfolder.child(name).delete().addOnSuccessListener {
                            imguu.removeAt(position)
                            names.removeAt(position)
                            dataP.imgurl.clear()
                            imguu.forEach {nn->
                                dataP.imgurl.add(hashMapOf("img" to nn.toString()))
                            }
                            var i=0
                            names.forEach {dd->
                                dataP.imgurl[i].put("name",dd)
                                i++
                            }

                            fire.set(dataP).addOnSuccessListener {
                                prog_delete.visibility = View.GONE
                                dialog3.cancel()
                                dialog2.cancel()
                                Toast.makeText(this,"تم الحذف بنجاح",Toast.LENGTH_SHORT).show()
                            }


                        }
                }

            }






        }




    }


    private fun BanalsUploud(images:ArrayList<Uri>) {
        val dp = FirebaseFirestore.getInstance().collection("panals")
        val newpostRef = "n4gMxzXo88sJFcEdHo1k"
        val urls = ArrayList<String>()



        prog_post_banals.visibility = View.VISIBLE

        var imegsfolder = FirebaseStorage.getInstance().getReference().child("photo").child(newpostRef)
        imegsfolder.listAll().addOnSuccessListener { listresult ->
            val uploadTasks = images.map { imageUri ->
                val ref = FirebaseStorage.getInstance().getReference().child("photo")
                    .child(newpostRef)
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
                            if(urls.size == images.size){

                                dp.document(newpostRef).get().addOnSuccessListener { panal->
                                    val bb = panal.toObject(Banals::class.java)!!

                                    urls.forEach { uuu->
                                        var mm = hashMapOf<String,String>("name" to "nun","img" to uuu)
                                        bb.imgurl.add(mm)
                                    }

                                    dp.document(newpostRef).set(bb).addOnSuccessListener {
                                        Toast.makeText(this,"تم تحميل الاعلانات بنجاح",Toast.LENGTH_SHORT).show()
                                        prog_post_banals.visibility = View.GONE
                                        finish()
                                    }

                                }







                            }

                        }
                    }
            }

            Tasks.whenAllComplete(uploadTasks)

        }



    }

    private fun headerSteup() {
        hedar_tital.text = "تغير الاعلانات"
        heder_bake.setOnClickListener {
            finish()
        }
    }

    private fun imelmnt() {

        imeg=ArrayList<Uri>()


        heder_ban = findViewById(R.id.heder_ban)
        banal_imag = findViewById(R.id.banal_imag)
        banal_prod = findViewById(R.id.banal_prod)
        banal_count = findViewById(R.id.banal_count)
        btn_edit_panal = findViewById(R.id.btn_edit_panal)
        btn_conect_b_p = findViewById(R.id.btn_conect_b_p)
        add_photo_banal = findViewById(R.id.add_photo_banal)
        prog_post_banals = findViewById(R.id.prog_post_banals)
        post_photo_banal = findViewById(R.id.post_photo_banal)
        chick_panal_prodact = findViewById(R.id.chick_panal_prodact)
        connect_banal_prodact = findViewById(R.id.connect_banal_prodact)

        vis_mult_panal = findViewById(R.id.vis_mult_panal)

        hedar_tital = heder_ban.findViewById(R.id.hedar_tital)
        heder_bake = heder_ban.findViewById(R.id.heder_bake)
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_list_img)


        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)

        list_img_n =dialog.findViewById(R.id.list_img_n)

        dialog2 = Dialog(this)
        dialog2.setContentView(R.layout.dialog_list_img)


        dialog2.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog2.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.window?.setGravity(Gravity.BOTTOM)

        list_img_n2 =dialog2.findViewById(R.id.list_img_n)


        btn_add_one_img = findViewById(R.id.btn_add_one_img)
        img_show =findViewById(R.id.img_show)

         chick_pan_only = findViewById(R.id.chick_pan_only)
         chick_delete_pan = findViewById(R.id.chick_delete_pan)
       btn_delete_list_banal =findViewById(R.id.btn_delete_list_banal)



        dialog3 =  Dialog(this)
        dialog3.setContentView(R.layout.dialog_delete_conferm)

        dialog3.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog3.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog3.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog3.window?.setGravity(Gravity.BOTTOM)

        prog_delete = dialog3.findViewById(R.id.prog_delete)
        btn_delete_cansel = dialog3.findViewById(R.id.btn_delete_cansel)
        btn_delete_conferm = dialog3.findViewById(R.id.btn_delete_conferm)


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
}