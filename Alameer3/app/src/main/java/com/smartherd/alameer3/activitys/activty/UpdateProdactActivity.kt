package com.smartherd.alameer3.activitys.activty

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.core.view.isNotEmpty
import com.google.android.gms.tasks.Tasks
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.adapders.CustomAdapterList
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


class UpdateProdactActivity : AppCompatActivity() {
    private lateinit var update_count: CheckBox
    private lateinit var update_price: CheckBox

    private lateinit var btn_update_price: Button
    private lateinit var btn_update_count: Button
    private lateinit var prog_update: ProgressBar
    private lateinit var update_descount: CheckBox
    private lateinit var heder_conter: LinearLayout
    private lateinit var update_prod_name: EditText
    private lateinit var btn_update_decount: Button
    private lateinit var summary_new_price: EditText
    private lateinit var summary_new_count: EditText
    private lateinit var input_update_price: EditText
    private lateinit var input_update_count: EditText
    private lateinit var btn_save_all_updates: Button
    private lateinit var summary_new_decount: EditText
    private lateinit var input_update_decount: EditText
    private lateinit var chos_size_update_price: Spinner
    private lateinit var chos_size_update_count: Spinner
    private lateinit var chos_size_update_descount: Spinner
    private lateinit var updata_price_card: androidx.cardview.widget.CardView
    private lateinit var updata_count_card: androidx.cardview.widget.CardView
    private lateinit var updata_count_discount: androidx.cardview.widget.CardView
    private lateinit var heder_bake:ImageView
    private lateinit var hedar_tital:TextView

    private lateinit var update_imaga:CheckBox
    private lateinit var update_prod_descrp:EditText
    private lateinit var updata_image_card:CardView
    private lateinit var chos_update_image:Spinner

    private lateinit var btn_add_new_img:Button
    private lateinit var imge_count_total:TextView
    private var imgCount = 0
    var str = kotlin.collections.ArrayList<String>()
    var count = 0
    var imgePostion = 0
    private lateinit var compressedImageUri: Uri

    private lateinit var imeg:ArrayList<Uri>
    private lateinit var newImeg:ArrayList<Uri>


    private lateinit var one_price:CheckBox
    private lateinit var spo_down:CheckBox
    private lateinit var input_d_price:EditText

    private lateinit var descount_forall:CheckBox
    private lateinit var count_forall:CheckBox
    private lateinit var update_add_sizes:CheckBox


    private lateinit var updata_add_size:CardView
    private lateinit var chick_new_one_price:CheckBox
    private lateinit var add_new_prod_price_input:EditText
    private lateinit var add_prod_new_spti_price:EditText
    private lateinit var btn_add_price_new:Button
    private lateinit var input_new_size_prodact:AutoCompleteTextView
    private lateinit var summary_size_new:EditText
    private lateinit var chick_price_down_new:CheckBox

    private lateinit var updata_color_card:CardView
    private lateinit var list_show_img_add:GridView
    private lateinit var dialog: Dialog
    private lateinit var dialog2: Dialog
    private lateinit var prog_delete: ProgressBar
    private lateinit var btn_delete_cansel: android.widget.Button
    private lateinit var btn_delete_conferm: android.widget.Button

    private lateinit var input_update_size:EditText
    private lateinit var ChipGroupSizeUpdate:ChipGroup
    private lateinit var chbox_update_sizes:CheckBox
    private lateinit var btn_update_size: android.widget.Button
    private lateinit var updata_size_card:CardView

    private lateinit var summary_update_colors:EditText
    private lateinit var btn_update_color:Button
    private lateinit var chose_size_newColor:Spinner
    private lateinit var chick_one_color_update:CheckBox

    private lateinit var input_update_color:EditText


    var reOrderImage = kotlin.collections.ArrayList<String>()

    private lateinit var list_img_n: GridView
    private var flagImg=0
    private var prodId=""

    var flag =false
    private lateinit var btn_show_img:Button
    private lateinit var btn_show_new_img:Button
    private lateinit var datePicker_desc:DatePicker
    private lateinit var chick_discount_time:CheckBox
    private lateinit var chick_update_colors:CheckBox

    private lateinit var btn_reorder_img:Button
    var loclImag = ArrayList<Uri>()


    var indS1=0

    private lateinit var chick_img_size:CheckBox
    private lateinit var chick_img_color:CheckBox
    private lateinit var updata_add_img_size:CardView
    private lateinit var btn_img_size:Button
    private lateinit var updata_add_img_color:CardView
    private lateinit var btn_img_color:Button
    private lateinit var dialog3:Dialog

    private lateinit var chick_update_name:CheckBox
    private lateinit var update_name_desc_parent:LinearLayout
    private lateinit var btn_updateSave_name:Button
    private lateinit var btn_save_new_secto:Button
    private lateinit var updata_prod_sect:CardView
    private lateinit var chos_prod_section:Spinner


    private lateinit var chbox_delete_sizes:CheckBox
    private lateinit var btn_delete_size:android.widget.Button
    private lateinit var ChipGroupSizeDelete:ChipGroup
    private lateinit var delete_size_card:CardView


    var updateSelectIndex = -1
    var deleteSelectIndex = -1
    var updateSelectString = ""
    var deleteSelectString = ""




    private lateinit var chick_new_section:CheckBox
    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){ uri->


        if(uri !=null) {
            GlobalScope.launch(Dispatchers.Main) {
                uri.forEach {tt->

                    val compressedImageFile = compress(this@UpdateProdactActivity,tt)
                    compressedImageUri = Uri.fromFile(compressedImageFile)

                    loclImag.add(compressedImageUri)
                    str.add((count++).toString())
                }
                imge_count_total.text = (loclImag.size + imeg.size).toString()

                var last_index = newImeg.size
                imgePostion = last_index-1
            }

        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_prodact)


        imelmnt()
        imeg = ArrayList<Uri>()
        newImeg = ArrayList<Uri>()
         prodId = intent.getStringExtra("prodid")!!

        getProdactItem(prodId)

        checkBoxLisener()

        btn_add_new_img.setOnClickListener {
            flagImg = 0
            getContent.launch("image/*")

        }






    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri

                newImeg[imgePostion] = resultUri
                Toast.makeText(this,"done !",Toast.LENGTH_SHORT)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this,"errer !",Toast.LENGTH_SHORT)

            }
        }
    }
    fun splitText(text: String): ArrayList<String> {
        val words = text.split("\n") // تقسيم النص إلى كلمات باستخدام الفاصلة,"
        return ArrayList(words) // تحويل القائمة إلى مصفوفة ArrayList وإرجاعها
    }

    fun splitText2(text: String): ArrayList<String> {
        val words = text.split("/") // تقسيم النص إلى كلمات باستخدام الفاصلة,"
        return ArrayList(words) // تحويل القائمة إلى مصفوفة ArrayList وإرجاعها
    }


    private fun checkBoxLisener() {

        chick_update_name.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                update_name_desc_parent.visibility = View.VISIBLE
                update_count.isChecked = false
                update_price.isChecked = false
                chbox_update_sizes.isChecked = false
                update_descount.isChecked = false
                update_imaga.isChecked = false
                update_add_sizes.isChecked=false
                chick_update_colors.isChecked = false
                chbox_delete_sizes.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false
                chick_new_section.isChecked = false



                btn_save_all_updates.visibility = View.GONE

            }
            else{
                update_name_desc_parent.visibility = View.GONE

            }
        }

        update_count.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                updata_count_card.visibility = View.VISIBLE
                btn_save_all_updates.visibility = View.VISIBLE

                chick_update_name.isChecked = false
                update_price.isChecked = false
                update_descount.isChecked = false
                chbox_update_sizes.isChecked = false
                chbox_delete_sizes.isChecked = false
                update_imaga.isChecked = false
                update_add_sizes.isChecked=false
                chick_update_colors.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false
                chick_new_section.isChecked = false

            } else {
                updata_count_card.visibility = View.GONE
            }

        }

        update_price.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                updata_price_card.visibility = View.VISIBLE
                btn_save_all_updates.visibility = View.VISIBLE

                chick_update_name.isChecked = false
                update_count.isChecked = false
                update_descount.isChecked = false
                chbox_update_sizes.isChecked = false
                chbox_delete_sizes.isChecked = false
                update_imaga.isChecked = false
                update_add_sizes.isChecked=false
                chick_update_colors.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false
                chick_new_section.isChecked = false

            } else
                updata_price_card.visibility = View.GONE
        }
        update_descount.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                updata_count_discount.visibility = View.VISIBLE
                btn_save_all_updates.visibility = View.VISIBLE

                chick_update_name.isChecked = false
                update_count.isChecked = false
                update_price.isChecked = false
                chbox_update_sizes.isChecked = false
                update_imaga.isChecked = false
                chbox_delete_sizes.isChecked = false
                update_add_sizes.isChecked=false
                chick_update_colors.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false
                chick_new_section.isChecked = false


            } else
                updata_count_discount.visibility = View.GONE

        }

        update_imaga.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                updata_image_card.visibility = View.VISIBLE
                btn_save_all_updates.visibility = View.VISIBLE

                chick_update_name.isChecked = false
                update_count.isChecked = false
                update_price.isChecked = false
                chbox_update_sizes.isChecked = false
                update_descount.isChecked = false
                chbox_delete_sizes.isChecked = false
                update_add_sizes.isChecked=false
                chick_update_colors.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false
                chick_new_section.isChecked = false

            }
            else{
                updata_image_card.visibility = View.GONE
            }
        }

        update_add_sizes.setOnCheckedChangeListener { buttonView, isChecked ->
            if(update_add_sizes.isChecked){
                updata_add_size.visibility = View.VISIBLE
                btn_save_all_updates.visibility = View.VISIBLE

                chick_update_name.isChecked = false
                update_count.isChecked = false
                update_price.isChecked = false
                chbox_update_sizes.isChecked = false
                chbox_delete_sizes.isChecked = false
                update_descount.isChecked = false
                update_imaga.isChecked=false
                chick_update_colors.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false
                chick_new_section.isChecked = false

            }
            else{
                updata_add_size.visibility  =View.GONE
            }
        }

        chick_update_colors.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                updata_color_card.visibility= View.VISIBLE
                btn_save_all_updates.visibility = View.VISIBLE


                chick_update_name.isChecked = false
                update_count.isChecked = false
                chbox_delete_sizes.isChecked = false
                update_price.isChecked = false
                update_descount.isChecked = false
                update_imaga.isChecked=false
                update_add_sizes.isChecked = false
                chbox_update_sizes.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false
                chick_new_section.isChecked = false

            }
            else{
                updata_color_card.visibility= View.GONE

            }
        }

        chick_discount_time.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                datePicker_desc.visibility = View.VISIBLE


            }
            else{
                datePicker_desc.visibility = View.GONE
            }
        }

        spo_down.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                input_d_price.visibility=View.VISIBLE
            }
            else{
                input_d_price.visibility = View.GONE
            }
        }

        chick_img_size.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                updata_add_img_size.visibility = View.VISIBLE
                btn_save_all_updates.visibility = View.VISIBLE

                chick_update_name.isChecked = false
                update_count.isChecked = false
                update_price.isChecked = false
                update_descount.isChecked = false
                update_imaga.isChecked=false
                update_add_sizes.isChecked = false
                chick_update_colors.isChecked = false
                chbox_delete_sizes.isChecked = false
                chick_img_color.isChecked = false
                chbox_update_sizes.isChecked = false
                chick_new_section.isChecked = false

            }
            else{
                updata_add_img_size.visibility = View.GONE
            }
        }

        chick_img_color.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                updata_add_img_color.visibility = View.VISIBLE
                btn_save_all_updates.visibility = View.VISIBLE

                chick_update_name.isChecked = false
                update_count.isChecked = false
                chbox_delete_sizes.isChecked = false
                update_price.isChecked = false
                update_descount.isChecked = false
                update_imaga.isChecked=false
                chbox_update_sizes.isChecked = false
                update_add_sizes.isChecked = false
                chick_update_colors.isChecked = false
                chick_img_size.isChecked = false
                chick_new_section.isChecked = false
            }
            else{
                updata_add_img_color.visibility = View.GONE
            }
        }


        chick_new_section.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                updata_prod_sect.visibility = View.VISIBLE
                chick_update_name.isChecked = false
                update_count.isChecked = false
                update_price.isChecked = false
                update_descount.isChecked = false
                update_imaga.isChecked=false
                chbox_delete_sizes.isChecked = false
                chbox_update_sizes.isChecked = false
                update_add_sizes.isChecked = false
                chick_update_colors.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false

            }
            else{
                updata_prod_sect.visibility = View.GONE
            }
        }

        chbox_update_sizes.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                updata_size_card.visibility = View.VISIBLE
                chick_update_name.isChecked = false
                update_count.isChecked = false
                update_price.isChecked = false
                update_descount.isChecked = false
                update_imaga.isChecked=false
                chbox_delete_sizes.isChecked = false
                chick_new_section.isChecked = false
                update_add_sizes.isChecked = false
                chick_update_colors.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false

            }
            else{
                updata_size_card.visibility = View.GONE
            }
        }

        chbox_delete_sizes.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                delete_size_card.visibility = View.VISIBLE
                chick_update_name.isChecked = false
                update_count.isChecked = false
                update_price.isChecked = false
                update_descount.isChecked = false
                update_imaga.isChecked=false
                chbox_update_sizes.isChecked = false
                chick_new_section.isChecked = false
                update_add_sizes.isChecked = false
                chick_update_colors.isChecked = false
                chick_img_size.isChecked = false
                chick_img_color.isChecked = false

            }
            else{
                delete_size_card.visibility = View.GONE
            }
        }

        chos_size_update_price.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                indS1 = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


    }



    private fun getProdactItem(prodId: String?) {
        var firestore = FirebaseFirestore.getInstance().collection("prodacts2")
        var dp1 = FirebaseFirestore.getInstance().collection("sections")
        firestore.document(prodId!!).get().addOnSuccessListener {
            if (it.exists()) {
                var data = it.toObject(Prodacts::class.java)!!
                var sizes = ArrayList<String>()
                var pricese = ArrayList<String>()
                var coloor = ArrayList<String>()
                data.sizes.forEach {arr->
                    arr.keys.forEach {si->
                        sizes.add(si)
                        pricese.add(arr[si].toString())



                    }

                }

                if(!data.colors.isEmpty()) {
                    data.colors.keys.forEach {
                        if(data.colors[it]!=null && !data.colors[it]!!.isEmpty())
                        data.colors[it]!!.forEach { co->
                            if (!coloor.contains(co)){
                                coloor.add(co)
                            }

                        }
                    }
                }

                setAdaptersForSpiner(sizes)




                updateSection(dp1, firestore, prodId)


                update_prod_name.setText(data.productName)
                update_prod_descrp.setText(data.about)

                updateNameDescrptin(firestore, prodId)



                displaySummry(data)

                var newDate = data
                var imString =ArrayList<String>()
                newDate.imageLinks.forEach { im->
                    var imm= Uri.parse(im)
                    imeg.add(imm)
                    imString.add(im)
                }


                imge_count_total.text = (newImeg.size + imeg.size).toString()








                imageMangment(coloor, prodId, newDate, sizes)



                updateDeleteSizes(newDate,prodId,sizes,pricese)


                saveLocalUpdateLissener(newDate)






            }
        }
    }

    private fun updateDeleteSizes(data: Prodacts, prodId: String,sizes:ArrayList<String>,pricese:ArrayList<String>) {

        for (size in sizes){
            val chip = createChip(size)
            ChipGroupSizeUpdate.addView(chip)

        }
        for (size in sizes){
            val chip = createChip(size)
            ChipGroupSizeDelete.addView(chip)

        }



        for (i in 0 until ChipGroupSizeDelete.childCount) {
            val chip = ChipGroupSizeDelete.getChildAt(i) as Chip
            chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    deleteSelectIndex = i
                    deleteSelectString = chip.text.toString()


                }
                else{
                    deleteSelectIndex =-1
                    deleteSelectString = ""
                }
            }
        }



        for (i in 0 until ChipGroupSizeUpdate.childCount) {
            val chip = ChipGroupSizeUpdate.getChildAt(i) as Chip
            chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    updateSelectIndex = i
                    updateSelectString = chip.text.toString()


                }
                else{
                    updateSelectIndex =-1
                    updateSelectString = ""
                }
            }
        }





        btn_update_size.setOnClickListener {

            if(updateSelectIndex!=-1 && input_update_size.text.toString().isNotEmpty()){

                prog_update.visibility = View.VISIBLE
                var oldSize = HashMap<String,String>()
                var newSize = HashMap<String,String>()

                oldSize.put(sizes[updateSelectIndex],pricese[updateSelectIndex])

                newSize.put(input_update_size.text.toString(),pricese[updateSelectIndex])


               var oldIndex  =  data.sizes.indexOf(oldSize)

                data.sizes[oldIndex] = newSize



                if (data.colors.containsKey(sizes[updateSelectIndex])){
                var oldColorSize = data.colors.get(sizes[updateSelectIndex])

                data.colors.remove(sizes[updateSelectIndex])
                data.colors.put(input_update_size.text.toString(),oldColorSize!!)
            }

            if(data.discount.containsKey(sizes[updateSelectIndex])){
                var oldDiscountSize = data.discount.get(sizes[updateSelectIndex])

                data.discount.remove(sizes[updateSelectIndex])
                data.discount.put(input_update_size.text.toString(),oldDiscountSize!!)
            }

            if(data.downprice.containsKey(sizes[updateSelectIndex])){
                var downprice = data.downprice.get(sizes[updateSelectIndex])

                data.downprice.remove(sizes[updateSelectIndex])
                data.downprice.put(input_update_size.text.toString(),downprice!!)
            }

            if(data.contn.containsKey(sizes[updateSelectIndex])){
                var contn = data.contn.get(sizes[updateSelectIndex])

                data.contn.remove(sizes[updateSelectIndex])
                data.contn.put(input_update_size.text.toString(),contn!!)
            }

            if(data.imgSize.containsKey(sizes[updateSelectIndex])){
                var imgSize = data.imgSize.get(sizes[updateSelectIndex])

                data.imgSize.remove(sizes[updateSelectIndex])
                data.imgSize.put(input_update_size.text.toString(),imgSize!!)
            }



                var firestore= FirebaseFirestore.getInstance().collection("prodacts2")
                firestore.document(prodId).set(data).addOnSuccessListener {
                    prog_update.visibility = View.GONE
                    Toast.makeText(this,"تم تغير المقاس بنجاح",Toast.LENGTH_SHORT).show()
                    finish()
                }




            }

        }


        btn_delete_size.setOnClickListener {

            if(deleteSelectIndex!=-1){
            prog_update.visibility = View.VISIBLE

                var oldSize = HashMap<String,String>()
                oldSize.put(sizes[deleteSelectIndex],pricese[deleteSelectIndex])

                if(data.sizes.size>1){

                    data.sizes.remove(oldSize)


                    if (data.colors.keys.contains(sizes[deleteSelectIndex])){
                        data.colors.remove(sizes[deleteSelectIndex])
            }

            if(data.discount.keys.contains(sizes[deleteSelectIndex])){
                data.discount.remove(sizes[deleteSelectIndex])

            }

            if(data.downprice.keys.contains(sizes[deleteSelectIndex])){
                data.downprice.remove(sizes[deleteSelectIndex])

            }

            if(data.contn.keys.contains(sizes[deleteSelectIndex])){

                data.contn.remove(sizes[deleteSelectIndex])

            }

            if(data.imgSize.keys.contains(sizes[deleteSelectIndex])){
                data.imgSize.remove(sizes[deleteSelectIndex])

            }
                    var firestore= FirebaseFirestore.getInstance().collection("prodacts2")
            firestore.document(prodId).set(data).addOnSuccessListener {
                prog_update.visibility = View.GONE
                Toast.makeText(this,"تم حذف المقاس بنجاح",Toast.LENGTH_SHORT).show()
                finish()
            }

                }
                else{
                    Snackbar.make(chbox_delete_sizes,"لا يمكن حذف المقاس لانه لا يوجد غيره",Snackbar.LENGTH_SHORT).show()
                }

            }

        }


    }
//    btn_delete_size.setOnClickListener {
//
//        if(deleteSelectIndex!=-1){
//
//            prog_update.visibility = View.VISIBLE
//            var oldSize = HashMap<String,String>()
//            var newSize = HashMap<String,String>()
//
//            oldSize.put(sizes[updateSelectIndex],pricese[updateSelectIndex])
//
//            newSize.put(input_update_size.text.toString(),pricese[updateSelectIndex])
//
//            var oldIndex  =  data.sizes.indexOf(oldSize)
//
//            data.sizes[oldIndex] = newSize
//
//
//            if (data.colors.keys.contains(sizes[updateSelectIndex])){
//                var oldColorSize = data.colors.get(sizes[updateSelectIndex])
//
//                data.colors.remove(sizes[updateSelectIndex])
//                data.colors.put(input_update_size.text.toString(),oldColorSize!!)
//            }
//
//            if(data.discount.keys.contains(sizes[updateSelectIndex])){
//                var oldDiscountSize = data.discount.get(sizes[updateSelectIndex])
//
//                data.discount.remove(sizes[updateSelectIndex])
//                data.discount.put(input_update_size.text.toString(),oldDiscountSize!!)
//            }
//
//            if(data.downprice.keys.contains(sizes[updateSelectIndex])){
//                var downprice = data.downprice.get(sizes[updateSelectIndex])
//
//                data.downprice.remove(sizes[updateSelectIndex])
//                data.downprice.put(input_update_size.text.toString(),downprice!!)
//            }
//
//            if(data.contn.keys.contains(sizes[updateSelectIndex])){
//                var contn = data.contn.get(sizes[updateSelectIndex])
//
//                data.contn.remove(sizes[updateSelectIndex])
//                data.contn.put(input_update_size.text.toString(),contn!!)
//            }
//
//            if(data.imgSize.keys.contains(sizes[updateSelectIndex])){
//                var imgSize = data.imgSize.get(sizes[updateSelectIndex])
//
//                data.imgSize.remove(sizes[updateSelectIndex])
//                data.imgSize.put(input_update_size.text.toString(),imgSize!!)
//            }
//
//
//
//            var firestore= FirebaseFirestore.getInstance().collection("prodacts2")
//            firestore.document(prodId).set(data).addOnSuccessListener {
//                prog_update.visibility = View.GONE
//                Toast.makeText(this,"تم تغير المقاس بنجاح",Toast.LENGTH_SHORT).show()
//                finish()
//            }
//
//
//
//
//        }
//
//    }


    fun createChip(size: String): Chip {
        val chip = Chip(this)
        chip.text = size
        chip.isClickable = true
        chip.isCheckable = true
        return chip
    }

    private fun imageMangment(coloor: ArrayList<String>, prodId: String?, newDate: Prodacts, sizes: ArrayList<String>) {
        btn_show_img.setOnClickListener {
            flagImg = 1
            var listImageAd = CustomAdapterList(this, imeg)
            list_img_n.adapter = listImageAd
            dialog.show()
        }

        btn_show_new_img.setOnClickListener {
            flagImg = 0
            var listImageAd = CustomAdapterList(this, newImeg)
            list_img_n.adapter = listImageAd
            dialog.show()
        }
        btn_reorder_img.setOnClickListener {
            flagImg = 2
            var listImageAd = CustomAdapterList(this, imeg)
            list_img_n.adapter = listImageAd
            dialog.show()
        }

        btn_img_size.setOnClickListener {
            flagImg = 3
            var listImageAd = CustomAdapterList(this, imeg)
            list_img_n.adapter = listImageAd
            dialog.show()
        }
        btn_img_color.setOnClickListener {
            if (coloor.size > 0) {
                flagImg = 4
                var listImageAd = CustomAdapterList(this, imeg)
                list_img_n.adapter = listImageAd
                dialog.show()
            } else {
                Snackbar.make(btn_img_color, "لا يوجد اللوان للمنتج ", Snackbar.LENGTH_SHORT).show()
            }
        }
        list_img_n.setOnItemClickListener { parent, view, position, id ->

            if (flagImg == 0) {
                val imageUri = newImeg[position]
                imgePostion = position
                CropImage.activity(imageUri).start(this@UpdateProdactActivity)

            } else if (flagImg == 1) {
                deleteOldPhotoFromProdact(position, prodId, newDate)

            } else if (flagImg == 2) {
                reOrderImage.add(imeg[position].toString())
                Toast.makeText(
                    this,
                    "ترتيب )" + reOrderImage.size.toString() + ")",
                    Toast.LENGTH_SHORT
                ).show()
                if (reOrderImage.size == imeg.size) {
                    Toast.makeText(this, "تم اكمال الترتيب بنجاح", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                    newDate.imageLinks.clear()
                    imeg.clear()
                    reOrderImage.forEach { mm ->
                        newDate.imageLinks.add(mm)
                        var imm = Uri.parse(mm)
                        imeg.add(imm)
                    }
                    reOrderImage.clear()
                }

            } else if (flagImg == 3) {

                var imgUri = position.toString()


                val tital_dialog = dialog3.findViewById<TextView>(R.id.tital_dialog);
                val choos_color_size_img = dialog3.findViewById<Spinner>(R.id.choos_color_size_img);
                val btn_finsh_connect_img =
                    dialog3.findViewById<AppCompatButton>(R.id.btn_finsh_connect_img);
                val btn_cansel_connect_img =
                    dialog3.findViewById<AppCompatButton>(R.id.btn_cansel_connect_img);

                tital_dialog.text = "اختار مقاس لربطة مع الصورة"
                choos_color_size_img.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes)

                dialog3.show()

                btn_finsh_connect_img.setOnClickListener {
                    newDate.imgSize.put(choos_color_size_img.selectedItem.toString(), imgUri)
                    Toast.makeText(this, "تم الربط !", Toast.LENGTH_SHORT).show()
                    dialog3.cancel()
                }


            } else if (flagImg == 4) {
                var imgUri = position.toString()

                val tital_dialog = dialog3.findViewById<TextView>(R.id.tital_dialog);
                val choos_color_size_img = dialog3.findViewById<Spinner>(R.id.choos_color_size_img);
                val btn_finsh_connect_img =
                    dialog3.findViewById<AppCompatButton>(R.id.btn_finsh_connect_img);
                val btn_cansel_connect_img =
                    dialog3.findViewById<AppCompatButton>(R.id.btn_cansel_connect_img);
                tital_dialog.text = "اختر لون لربطة مع الصورة"

                choos_color_size_img.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, coloor)
                dialog3.show()

                btn_finsh_connect_img.setOnClickListener {
                    newDate.imgColor.put(choos_color_size_img.selectedItem.toString(), imgUri)
                    Toast.makeText(this, "تم الربط !", Toast.LENGTH_SHORT).show()
                    dialog3.cancel()
                }

            }

        }
    }

    private fun setAdaptersForSpiner(sizes: ArrayList<String>) {
        var ad1 = ArrayAdapter(this, R.layout.spiner_layoyt, sizes)
        ad1.setDropDownViewResource(R.layout.siper2)
        chos_size_update_price.adapter = ad1


        var ad2 = ArrayAdapter(this, R.layout.spiner_layoyt, sizes)
        ad2.setDropDownViewResource(R.layout.siper2)
        chos_size_update_descount.adapter = ad2


        var ad3 = ArrayAdapter(this, R.layout.spiner_layoyt, sizes)
        ad3.setDropDownViewResource(R.layout.siper2)
        chos_size_update_count.adapter = ad3


        var ad4 = ArrayAdapter(this, R.layout.spiner_layoyt, sizes)
        ad4.setDropDownViewResource(R.layout.siper2)
        chose_size_newColor.adapter = ad4
    }

    private fun updateNameDescrptin(firestore: CollectionReference, prodId: String?) {
        btn_updateSave_name.setOnClickListener {
            if (update_prod_name.text.toString().isNotEmpty()) {
                var newName = update_prod_name.text.toString()
                prog_update.visibility = View.VISIBLE
                firestore.document(prodId!!).update("productName", newName).addOnSuccessListener {
                    if (update_prod_descrp.text.toString().isNotEmpty()) {
                        var newDesc = update_prod_descrp.text.toString()
                        firestore.document(prodId!!).update("about", newDesc).addOnSuccessListener {
                            prog_update.visibility = View.GONE
                            Toast.makeText(this, "تم التحديث بنجاح", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } else {
                        prog_update.visibility = View.GONE
                        Toast.makeText(this, "تم التحديث بنجاح", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun updateSection(dp1: CollectionReference, firestore: CollectionReference, prodId: String?) {
        var arrSectionName = ArrayList<String>()
        var arrSectionID = ArrayList<String>()

        dp1.orderBy("order").get().addOnSuccessListener {
            it.documents.forEach { secc ->
                arrSectionName.add(secc.get("name").toString())
                arrSectionID.add(secc.id)
            }

            var ad6 = ArrayAdapter(this, R.layout.spiner_layoyt, arrSectionName)
            ad6.setDropDownViewResource(R.layout.siper2)
            chos_prod_section.adapter = ad6
            var secIndex = -1
            chos_prod_section.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    secIndex = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

            btn_save_new_secto.setOnClickListener {
                if (secIndex != -1) {
                    prog_update.visibility = View.GONE
                    var newSec = arrSectionID[secIndex]
                    firestore.document(prodId!!).update("category", newSec).addOnSuccessListener {
                        prog_update.visibility = View.GONE
                        Toast.makeText(this, "تم التحديث بنجاح", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun saveLocalUpdateLissener(newDate: Prodacts) {
        btn_update_count.setOnClickListener {
            if (input_update_count.text.toString().isNotEmpty())
            {

                if(count_forall.isChecked){
                    var newCount = input_update_count.text.toString()
                    newDate.contn.keys.forEach {ss->
                        newDate.contn.put(ss,newCount)
                    }

                }
                else{
                    var newCount = input_update_count.text.toString()
                    var choseSize = chos_size_update_count.selectedItem.toString()
                    newDate.contn.put(choseSize, newCount)
                }



                displaySummry(newDate)

            }
        }

        btn_update_decount.setOnClickListener {

            if(input_update_decount.text.toString().isNotEmpty()){
                if(chick_discount_time.isChecked){
                    val year = datePicker_desc.year
                    val month = datePicker_desc.month
                    val day = datePicker_desc.dayOfMonth
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, day)
                    val fullDate = calendar.time

                    newDate.expirDesc = fullDate

                    if(descount_forall.isChecked){
                    var newDecount = input_update_decount.text.toString()
                    newDate.sizes.forEach { arr->
                        arr.keys.forEach {ss->
                            newDate.discount.put(ss,newDecount)

                        }
                    }

                }
                else{
                    var newDecount = input_update_decount.text.toString()
                    var choseSize = chos_size_update_descount.selectedItem.toString()
                    newDate.discount.put(choseSize, newDecount)
                }

                }
                else{
                    if(descount_forall.isChecked){
                        var newDecount = input_update_decount.text.toString()
                        newDate.sizes.forEach { arr->
                            arr.keys.forEach {ss->
                                newDate.discount.put(ss,newDecount)

                            }
                        }

                    }
                    else{
                        var newDecount = input_update_decount.text.toString()
                        var choseSize = chos_size_update_descount.selectedItem.toString()
                        newDate.discount.put(choseSize, newDecount)
                    }
                }
                displaySummry(newDate)
                newDate.dateUpdate = Calendar.getInstance().time

            }




        }

        btn_update_price.setOnClickListener {
            if (input_update_price.text.toString().isNotEmpty() && chos_size_update_price.selectedItem != null) {

                var new_price = input_update_price.text.toString()
                if(one_price.isChecked){


                    newDate.sizes.forEach {aa->
                        aa.keys.forEach { ss->
                            aa.put(ss,new_price)

                        }

                    }



                }
                else{
                    var choseSize = chos_size_update_price.selectedItem.toString()
                    var newprice = input_update_price.text.toString()
                    newDate.sizes[indS1].put(choseSize,newprice)

                }


            }
            if(spo_down.isChecked){
                if(one_price.isChecked){
                    var sp_price= input_d_price.text.toString()
                    newDate.sizes.forEach {nn->
                        nn.keys.forEach { kk->
                            newDate.downprice.put(kk,sp_price)
                        }

                    }
                }else{
                    var choseSize = chos_size_update_price.selectedItem.toString()
                    var d_price = input_d_price.text.toString()
                    newDate.downprice.put(choseSize,d_price)
                }


            }
            displaySummry(newDate)
        }

        btn_add_price_new.setOnClickListener {
            if (input_new_size_prodact.text.toString()
                    .isNotEmpty() && add_new_prod_price_input.text.toString().isNotEmpty()
            ) {
                var price = add_new_prod_price_input.text.toString()
                var sizes = input_new_size_prodact.text.toString()

                var cc=0
                newDate.sizes.forEach {mm->
                    var m = mm.keys.first()
                    if (m!=sizes){
                        cc++
                    }
                    else{
                        mm[sizes] = price
                    }

                }
                if (cc == newDate.sizes.size){
                    newDate.sizes.add(hashMapOf(sizes to price))
                    newDate.contn.put(sizes, "100")

                }


                if (chick_price_down_new.isChecked) {
                    if (add_prod_new_spti_price.text.toString().isNotEmpty()) {
                        newDate.downprice.put(sizes, add_prod_new_spti_price.text.toString())

                    }


                }


            }
            displaySummry(newDate)
        }




        btn_update_color.setOnClickListener {
            if(input_update_color.text.toString().isNotEmpty()){
                var color = splitText(input_update_color.text.toString())
                if(chick_one_color_update.isChecked){
                   newDate.sizes.forEach {arr->
                       arr.keys.forEach {ss->
                           newDate.colors.put(ss,color)

                       }

                   }
                }
                else{
                    var selSiz = chose_size_newColor.selectedItem.toString()
                    newDate.colors.put(selSiz,color)
                }

                displaySummry(newDate)
            }
        }

        btn_save_all_updates.setOnClickListener {
            upLoudNewProdact( prodId, newDate)
        }




    }


    private fun deleteOldPhotoFromProdact(
        position: Int,
        prodId: String?,
        newDate: Prodacts
    ) {
        val imageUri = imeg[position]
        imgePostion = position
        val fullPath = imageUri.lastPathSegment
        val photoFullName = splitText2(fullPath.toString())
        val name = photoFullName[2]
        dialog2.show()

        btn_delete_conferm.setOnClickListener {

            prog_delete.visibility = View.VISIBLE

            var storeg = FirebaseStorage.getInstance()
            var imagesFolder = storeg.getReference().child("photo2").child(prodId!!)
            var imageRef = imagesFolder.child(name)
            imageRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "تم حذف الصورة بنجاح يرجى الانتظار", Toast.LENGTH_SHORT)
                    prog_delete.visibility = View.GONE
                    imeg.removeAt(imgePostion)
                    newDate.imageLinks.clear()
                    imeg.forEach { nn ->
                        newDate.imageLinks.add(nn.toString())
                    }
                    firestoreUplude(FirebaseFirestore.getInstance(), prodId, newDate)

                }
                .addOnFailureListener {
                    Toast.makeText(this, "حدث خطاء", Toast.LENGTH_SHORT)
                    dialog2.cancel()



                }


        }
    }

    private fun upLoudNewProdact(

        prodId: String?,
        newDate: Prodacts
    ) {

            prog_update.visibility = View.VISIBLE
            var firestore = FirebaseFirestore.getInstance()
            newDate.productName = update_prod_name.text.toString()
            newDate.about = update_prod_descrp.text.toString()

                if(newImeg.size>0){
                    var firebaseStorage = FirebaseStorage.getInstance()
                    val urls =ArrayList<String>()
                    val uploadTasks = newImeg.map { imageUri ->
                        Toast.makeText(this,"2",Toast.LENGTH_SHORT)

                        val ref = firebaseStorage.getReference().child("photo2")
                            .child(prodId!!)
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

                                    if (urls.size == newImeg.size) {

                                        urls.forEach { newll ->
                                            newDate.imageLinks.add(newll)
                                        }
                                        firestoreUplude(firestore, prodId, newDate)

                                    }

                                }
                            }
                    }
                    Tasks.whenAllComplete(uploadTasks)
                }
                else{
                    if(newDate.imageLinks.size>0){
                    firestoreUplude(firestore, prodId, newDate)
                }
                    else{
                        Snackbar.make(btn_save_all_updates,"لا يمكن رفع المنتج بدون صور",Snackbar.LENGTH_LONG).show()
                    }
                }









            }









    private fun firestoreUplude(
        firestore: FirebaseFirestore,
        prodId: String?,
        newDate: Prodacts
    ) {
        firestore.collection("prodacts2").document(prodId!!).set(newDate)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    prog_update.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "تمت الاضافة بنجاح!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                    dialog2.cancel()

                    dialog.cancel()
                }
                dialog2.cancel()

                dialog.cancel()
            }
    }




    private fun displaySummry(data: Prodacts) {
        summary_new_count.text.clear()
        summary_new_decount.text.clear()
        summary_new_price.text.clear()
        summary_size_new.text.clear()
        summary_update_colors.text.clear()

        data.contn.forEach {
            var str = summary_new_count.text.toString() + "\n" + it.key + " = " + it.value
            summary_new_count.setText(str)
        }




        if (data.discount.isNotEmpty()) {
            data.discount.forEach {
                var str = summary_new_decount.text.toString() + "\n" + it.key + " = " + it.value
                summary_new_decount.setText(str)
            }
        }



        if(data.sizes.size>0){

            data.sizes.forEach {aar->
                aar.keys.forEach {ss->
                    summary_size_new.setText(summary_size_new.text.toString()+"\n" + ss+" = " +aar[ss].toString())
                    summary_new_price.setText(summary_new_price.text.toString()+"\n"+ss + " = " + aar[ss].toString())

                }

            }

        }

        if (!data.downprice.isEmpty()){
            var str = summary_new_price.text.toString() + "\n" +"اسعار الجنوب : " +"\n"
            summary_new_price.setText(str)
            data.downprice.forEach {
                var str = summary_new_price.text.toString() + "\n" + it.key + " = " + it.value
                summary_new_price.setText(str)
            }
            summary_size_new.setText( summary_new_price.text.toString())
        }

        if(!data.colors.isEmpty()){

            data.colors.keys.forEach {ss->
                summary_update_colors.setText("\n" +summary_update_colors.text.toString() + ss+" = ")
                data.colors[ss]!!.forEach {colo->
                    summary_update_colors.setText(summary_update_colors.text.toString() + colo +" , ")

                }
            }
        }



        Toast.makeText(this,"تم الحفظ",Toast.LENGTH_SHORT).show()
    }

    private fun imelmnt() {
        prog_update = findViewById(R.id.prog_update)
        update_count = findViewById(R.id.update_count)
        update_price = findViewById(R.id.update_price)
        update_descount = findViewById(R.id.update_descount)
        update_prod_name = findViewById(R.id.update_prod_name)
        btn_update_price = findViewById(R.id.btn_update_price)
        btn_update_count = findViewById(R.id.btn_update_count)
        updata_price_card = findViewById(R.id.updata_price_card)
        summary_new_price = findViewById(R.id.summary_new_price)
        updata_count_card = findViewById(R.id.updata_count_card)
        summary_new_count = findViewById(R.id.summary_new_count)
        input_update_price = findViewById(R.id.input_update_price)
        btn_update_decount = findViewById(R.id.btn_update_decount)
        input_update_count = findViewById(R.id.input_update_count)
        summary_new_decount = findViewById(R.id.summary_new_decount)
        input_update_decount = findViewById(R.id.input_update_decount)
        btn_save_all_updates = findViewById(R.id.btn_save_all_updates)
        updata_count_discount = findViewById(R.id.updata_count_discount)
        chos_size_update_price = findViewById(R.id.chos_size_update_price)
        chos_size_update_count = findViewById(R.id.chos_size_update_count)
        chos_size_update_descount = findViewById(R.id.chos_size_update_descount)



        update_add_sizes = findViewById(R.id.update_add_sizes)
        chick_update_colors = findViewById(R.id.chick_update_colors)
        summary_update_colors = findViewById(R.id.summary_update_colors)

        heder_conter = findViewById(R.id.heder_conter)

        heder_bake = heder_conter.findViewById(R.id.heder_bake)
        hedar_tital = heder_conter.findViewById(R.id.hedar_tital)
        hedar_tital.text = "تعديل منتج"


       update_imaga = findViewById(R.id.update_imaga)
         update_prod_descrp = findViewById(R.id.update_prod_descrp)
         updata_image_card = findViewById(R.id.updata_image_card)
       chos_update_image =  findViewById(R.id.chos_update_image)
         btn_add_new_img = findViewById(R.id.btn_add_new_img)
       imge_count_total = findViewById(R.id.imge_count_total)

         chick_new_one_price = findViewById(R.id.chick_new_one_price)
        add_new_prod_price_input =findViewById(R.id.add_new_prod_price_input)
        add_prod_new_spti_price = findViewById(R.id.add_prod_new_spti_price)
         btn_add_price_new = findViewById(R.id.btn_add_price_new)
         summary_size_new = findViewById(R.id.summary_size_new)
        chick_price_down_new = findViewById(R.id.chick_price_down_new)
        input_new_size_prodact = findViewById(R.id.input_new_size_prodact)

        updata_add_size = findViewById(R.id.updata_add_size)


        one_price = findViewById(R.id.one_price)
         spo_down = findViewById(R.id.spo_down)
        input_d_price = findViewById(R.id.input_d_price)


        updata_color_card = findViewById(R.id.updata_color_card)
        count_forall = findViewById(R.id.count_forall)
        descount_forall = findViewById(R.id.descount_forall)


        btn_show_new_img = findViewById(R.id.btn_show_new_img)

      btn_update_color = findViewById(R.id.btn_update_color)
        chose_size_newColor = findViewById(R.id.chose_size_newColor)
         chick_one_color_update = findViewById(R.id.chick_one_color_update)

         input_update_color = findViewById(R.id.input_update_color)
        datePicker_desc = findViewById(R.id.datePicker_desc)
        chick_discount_time = findViewById(R.id.chick_discount_time)

        chbox_delete_sizes = findViewById(R.id.chbox_delete_sizes)
        delete_size_card = findViewById(R.id.delete_size_card)
        ChipGroupSizeDelete = findViewById(R.id.ChipGroupSizeDelete)
        btn_delete_size= findViewById(R.id.btn_delete_size)






        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_list_img)


        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)



        list_img_n =dialog.findViewById(R.id.list_img_n)
        btn_show_img = findViewById(R.id.btn_show_img)



        dialog2 =  Dialog(this)
        dialog2.setContentView(R.layout.dialog_delete_conferm)

		prog_delete = dialog2.findViewById(R.id.prog_delete)
		btn_delete_cansel = dialog2.findViewById(R.id.btn_delete_cansel)
		btn_delete_conferm = dialog2.findViewById(R.id.btn_delete_conferm)


        dialog2.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog2.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.window?.setGravity(Gravity.BOTTOM)

        btn_reorder_img = findViewById(R.id.btn_reorder_img)


        chick_img_size =findViewById(R.id.chick_img_size)
         chick_img_color = findViewById(R.id.chick_img_color)
        updata_add_img_size = findViewById(R.id.updata_add_img_size)
         btn_img_size = findViewById(R.id.btn_img_size)
       updata_add_img_color = findViewById(R.id.updata_add_img_color)
         btn_img_color = findViewById(R.id.btn_img_color)

        updata_size_card = findViewById(R.id.updata_size_card)
        btn_update_size = findViewById(R.id.btn_update_size)
        ChipGroupSizeUpdate = findViewById(R.id.ChipGroupSizeUpdate)

        chbox_update_sizes=findViewById(R.id.chbox_update_sizes)
        input_update_size = findViewById(R.id.input_update_size)


        dialog3 = Dialog(this)
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog3.setContentView(R.layout.dialog_img_color_size)



        dialog3.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog3.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog3.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog3.window?.setGravity(Gravity.CENTER)


        chick_update_name = findViewById(R.id.chick_update_name)
         update_name_desc_parent = findViewById(R.id.update_name_desc_parent)
        btn_updateSave_name = findViewById(R.id.btn_updateSave_name)


        btn_save_new_secto = findViewById(R.id.btn_save_new_secto)
        updata_prod_sect= findViewById(R.id.updata_prod_sect)
         chos_prod_section= findViewById(R.id.chos_prod_section)

        chick_new_section = findViewById(R.id.chick_new_section)
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


}