package com.smartherd.alameer3.activitys.activty

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ShareCompat
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.ablanco.zoomy.Zoomy
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.Helpers.ChickerFunction
import com.smartherd.alameer3.activitys.Helpers.ImageViewPigar
import com.smartherd.alameer3.activitys.MainActivity
import com.smartherd.alameer3.activitys.adapders.ProdactAdapter
import com.smartherd.alameer3.activitys.models.Clat
import com.smartherd.alameer3.activitys.models.ClatProdact
import com.smartherd.alameer3.activitys.models.Favorite
import com.smartherd.alameer3.activitys.models.Prodacts
import com.smartherd.alameer3.activitys.models.UserDate
import com.smartherd.alameer3.activitys.serves.GetSalah
import com.smartherd.alameer3.activitys.serves.GetSections
import com.smartherd.alameer3.activitys.serves.ProdactServes
import com.smartherd.alameer3.activitys.serves.UserServes
import com.squareup.picasso.Picasso
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ShowActivity : AppCompatActivity() {


    private var flagCount = false
    private var price = ""
    private var size =""
    private var descount = ""
    private var finalStatic = ""

    var prcet = 0
    private lateinit var dialog:Dialog
    private lateinit var showDialog: Dialog
    private lateinit var dialog2: Dialog
    private lateinit var r_show: RadioButton
    private lateinit var r_hidden: RadioButton
    private lateinit var prog_hidin: ProgressBar
    private lateinit var btn_hidn_cansel: Button
    private lateinit var btn_hidn_conferm: Button
    private lateinit var R_prod_hid_show: RadioGroup
    private lateinit var bakc_show:ImageView
    private lateinit var sal_icon_show:ImageView
    val decimalFormat = DecimalFormat("0.00", DecimalFormatSymbols(Locale.US))


    private lateinit var currentDate: Date
    private lateinit var bakc_Image: ImageView
    private lateinit var imgViewShowALl: ImageView
    private lateinit var tol_bar_image: LinearLayout




    private lateinit var txtsize: TextView

    private lateinit var order_mach: TextView
    private lateinit var l_size: LinearLayout
    private lateinit var total_cost: TextView
    private lateinit var countss: LinearLayout
    private lateinit var l_color: LinearLayout
    private lateinit var scrol_show: ScrollView
    private lateinit var section_name: TextView
    private lateinit var l_Totoale: LinearLayout
    private lateinit var sec_total: LinearLayout
    private lateinit var price_des: LinearLayout
    private lateinit var timer_end_text: TextView
    private lateinit var show_prod_name: TextView
    private lateinit var show_prod_price: TextView
    private lateinit var show_prod_about: TextView
    private lateinit var tezt_show_total: TextView
    private lateinit var show_prod_prcent: TextView
    private lateinit var linearLayout4: LinearLayout
    private lateinit var show_prod_hasmuch: TextView
    private lateinit var l_Totoal:LinearLayout
    private lateinit var show_prod_decount: TextView
    private lateinit var linearLayout2: LinearLayout
    private lateinit var show_sizes_prodacts: Spinner
    private lateinit var show_color_prodacts: Spinner
    private lateinit var admin_prodactMa: LinearLayout
    private lateinit var prog_add_to_cart: ProgressBar
    private lateinit var count_add: android.widget.Button
    private lateinit var count_minas: android.widget.Button
    private lateinit var btn_buy_now: android.widget.Button
    private lateinit var btn_prod_state: android.widget.Button
    private lateinit var btn_add_to_bag: android.widget.Button
    private lateinit var btn_prod_delete: android.widget.Button
    private lateinit var btn_prod_update: android.widget.Button
    private lateinit var shim_show: com.facebook.shimmer.ShimmerFrameLayout
    private lateinit var list_show_1: androidx.recyclerview.widget.RecyclerView
    private lateinit var view_pager_img_show: androidx.viewpager.widget.ViewPager
    private lateinit var ChipGroupSize_: com.google.android.material.chip.ChipGroup
    private lateinit var ChipGroupColor_: com.google.android.material.chip.ChipGroup
    private lateinit var worm_dots_indicator: com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator




    private lateinit var stringa: LinearLayout
    private lateinit var toolbar_tital: TextView
    private lateinit var share_prodact: ImageView
    private lateinit var sala_count_show: TextView
    private lateinit var hart_icon_show: ImageView
    private lateinit var salah_short_show: LinearLayout



    private lateinit var delet_text: TextView
    private lateinit var prog_delete: ProgressBar
    private lateinit var btn_delete_cansel: android.widget.Button
    private lateinit var btn_delete_conferm: android.widget.Button



    var indexOfSize = 0
    var indexOfColor =0

    var flagSelect = 0
    var flagColorSelection = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_show)









        imelmnt()


        val prodactId = intent.getStringExtra("prodId")
        val dolar = intent.getStringExtra("dolar")
        val userLoc = intent.getStringExtra("userLoc")
        val sectionID = intent.getStringExtra("section")
        chick()
        Log.d("id",prodactId.toString())

        val firestore = FirebaseFirestore.getInstance()
        currentDate =  Calendar.getInstance().time
        list_show_1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        ChickerFunction().changeStatusBarColor("#181818",window)
        getProdactItem(firestore, prodactId, dolar,sectionID,userLoc)
        countAddMinass()
        adminDeleteProdauct(prodactId!!)

        btn_prod_update.setOnClickListener {
            var intent = Intent(this,UpdateProdactActivity::class.java)
            intent.putExtra("prodid",prodactId)
            startActivity(intent)
        }

        bakc_show.setOnClickListener {
            finish()
        }

        GetSalah().salahCount(sala_count_show)

        salah_short_show.setOnClickListener {

            var intent = Intent(this,MainActivity::class.java)
            intent.putExtra("clatFlage","somd")
            startActivity(intent)
        }



        share_prodact.setOnClickListener {
            Toast.makeText(this,"يرجى الانتظار",Toast.LENGTH_SHORT).show()
            var urii = Uri.parse("https://alamir-store.web.app/prodact?=$prodactId")



            var sshortTask = FirebaseDynamicLinks.getInstance().createDynamicLink().setLink(urii).setDomainUriPrefix("https://alamirstore.page.link").setAndroidParameters(DynamicLink.AndroidParameters.Builder().build()).buildDynamicLink()

            FirebaseDynamicLinks.getInstance().createDynamicLink().setLongLink(sshortTask.uri).buildShortDynamicLink().addOnCompleteListener {task->
                if (task.isSuccessful){
                    var newLink = task.result.shortLink

                    Toast.makeText(this,"تم ",Toast.LENGTH_SHORT).show()
                    var masseg = "منتج مميز في متجر الامير تحقق منه الان \n ${newLink.toString()}"

                    var intetn = Intent(Intent.ACTION_SEND)
                    intetn.type = "text/plain"
                    intetn.putExtra(Intent.EXTRA_TEXT,masseg)

                    startActivity(Intent.createChooser(intetn,"مشاركة المنتج عبر .."))

                }
                else{
                    task.result.toString()
                    Toast.makeText(this,"فشل : ${task.result.toString()} ",Toast.LENGTH_SHORT).show()

                }


            }
        }






    }

    private fun setAnamition() {
        val animation = TranslateAnimation(0f, 0f, -200f, 0f)
        animation.duration = 1200
        animation.fillAfter = true
        view_pager_img_show.startAnimation(animation)


        val animation2 = TranslateAnimation(-200f, 0f, 0f, 0f)
        animation2.duration = 1200
        animation2.fillAfter = true


        show_prod_name.animation = animation2
        show_prod_about.animation = animation2
        show_prod_price.animation = animation2
        show_prod_decount.animation = animation2
        show_prod_hasmuch.animation = animation2
    }


    private fun adminDeleteProdauct(prodactId: String) {
        btn_prod_delete.setOnClickListener {
            dialog.show()
        }
        btn_delete_conferm.setOnClickListener {
            var storeg = FirebaseStorage.getInstance()
            var fire = FirebaseFirestore.getInstance()
            var clatstore = FirebaseFirestore.getInstance().collection("calt")
            val dp = fire.collection("prodacts2").document(prodactId)
            var imegsfolder = storeg.getReference().child("photo2").child(prodactId)
            imegsfolder.listAll().addOnSuccessListener { listresult ->
                for (img in listresult.items) {
                    img.delete()
                }
                Toast.makeText(this, "تم حذف الصور ويتم الان حذف باقي المنشور سيسغرق وقت يرجى الانتظار", Toast.LENGTH_SHORT).show()

                dp.delete().addOnSuccessListener {


                    Toast.makeText(this, "تم حذف المنتج بنجاح", Toast.LENGTH_SHORT).show()
                    prog_delete.visibility = View.GONE
                    dialog.cancel()
                }

            }

        }
    }

    private fun getProdactItem(firestore: FirebaseFirestore, prodactId: String?, dolar: String?,section:String?,userLoc:String?)
    {

        var sizes = ArrayList<String>()
        var prisecs = ArrayList<String>()
        ProdactServes().getProdactById(prodactId!!){prodact ->
            if(prodact!=null){
                displayProdactItem(prodact, sizes, prisecs)

                colorAndSizeSelectChing(prodact, dolar,userLoc)

                getSizeAndColors(prodact,dolar,userLoc)
                addToCallt(prodactId,prodact.state,prodact)

                adminHiddenShowProdact(prodact, firestore, prodactId)




                shim_show.stopShimmer()
                // تطبيق الأنيميشن للاختفاء السلس
                shim_show.animate()
                    .alpha(0f) // جعل الشفافية 0 (الاختفاء)
                    .setDuration(500) // مدة الأنيميشن بالمللي ثانية
                    .withEndAction {
                        // بعد انتهاء الأنيميشن، اجعل العنصر غير مرئي
                        shim_show.visibility = View.GONE
                        linearLayout4.visibility = View.VISIBLE // إظهار البيانات المطلوبة
                        linearLayout2.visibility = View.VISIBLE
                    }
                    .start()
            }


            if(FirebaseAuth.getInstance().currentUser!=null){
                var useid = FirebaseAuth.getInstance().currentUser!!.uid
                firestore.collection("favorite").document(useid).get().addOnSuccessListener {favu->
                    if(favu.exists()){
                        var listFavorite= favu.toObject(Favorite::class.java)!!
                        if(listFavorite.favlist.contains(prodactId)){
                            hart_icon_show.setImageResource(R.drawable.hart_on)

                        }
                    }

                    hart_icon_show.setOnClickListener {
                        var str =""
                        var listFavorite:Favorite
                        if(favu.exists()){
                            listFavorite= favu.toObject(Favorite::class.java)!!
                            if(!listFavorite.favlist.contains(prodactId)){
                                listFavorite.favlist.add(prodactId)
                                hart_icon_show.setImageResource(R.drawable.hart_on)
                                str = "تمت الاضافة الى المفضلة"

                            }
                            else{
                                listFavorite.favlist.remove(prodactId)
                                hart_icon_show.setImageResource(R.drawable.hart_of)
                                str = "تمت الازالة من المفضلة"
                            }
                        }
                        else{
                            listFavorite =  Favorite()
                            listFavorite.favlist.add(prodactId)
                            hart_icon_show.setImageResource(R.drawable.hart_on)
                            str = "تمت الاضافة الى المفضلة"
                        }

                        firestore.collection("favorite").document(useid).set(listFavorite).addOnSuccessListener {
                            Toast.makeText(this,str,Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }

            var sect = ""
            if(section!=null){
                sect = section
            }
            else if (section==null || prodact!=null){
                sect = prodact!!.category
            }


            GetSections().getSectionByID(sect){section ->
                if(section!=null){
                    section_name.text = section.name
                }

            }


            ProdactServes().getProdactBySection(dolar!!,sect,true,10){prodactItemView ->
                var cc = 0
                if (prodactItemView.prodacts.size > 9)
                    cc = 9
                else if (prodactItemView.prodacts.size <= 9)
                    cc =  prodactItemView.prodacts.size

                var adapter = ProdactAdapter(prodactItemView.prodacts,prodactItemView.prodaID,cc,dolar!!,userLoc!!)
                list_show_1.adapter = adapter
            }


        }







    }

    private fun getSizeAndColors(prodact: Prodacts, dolar: String?, userLoc: String?) {

        var sizes=ArrayList<String>()
       var  prisecs=ArrayList<String>()
        var Size_price = prodact.sizes

        Size_price.forEach { ma->
            ma.keys.forEach {ss->
                sizes.add(ss.toString())

            }
            ma.values.forEach {vv->
                prisecs.add(vv)
            }

        }

        for (size in sizes) {
            val chip = createChip(size)
            ChipGroupSize_.addView(chip)
        }


        var selectSize = ""

        val defe = ChipGroupSize_.getChildAt(0) as Chip

        selectSize =   sizes[0]
        val stack = sizes[0]

        defultSelection(prodact, selectSize, dolar,userLoc)

        colorSeelectionGet(prodact,selectSize,dolar,userLoc)

        show_prod_hasmuch.text = prodact.contn.get(selectSize).toString()

        if (prodact.contn.get(selectSize).toString().toInt() >15){
            l_Totoale.visibility = View.GONE
        }
        else{
            l_Totoale.visibility = View.VISIBLE
        }



        if (ChipGroupSize_.childCount == 1 && defe.text.toString() == ".."){
            flagSelect = 1
            txtsize.visibility = View.GONE
            ChipGroupSize_.visibility = View.GONE
        }




        // إضافة مستمع لكل Chip
        for (i in 0 until ChipGroupSize_.childCount) {
            val chip = ChipGroupSize_.getChildAt(i) as Chip
            chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    indexOfSize = i
                    selectSize = chip.text.toString()
                    flagSelect = 1
                    defultSelection(prodact, selectSize, dolar,userLoc)

                    colorSeelectionGet(prodact,selectSize,dolar,userLoc)

                    show_prod_hasmuch.text = prodact.contn.get(selectSize).toString()

                    if (prodact.contn.get(selectSize).toString().toInt() >15){
                        l_Totoale.visibility = View.GONE
                    }
                    else{
                        l_Totoale.visibility = View.VISIBLE
                    }


                    if(!prodact.imgSize.isEmpty()){
                        val ppo =  prodact.imgSize.get(selectSize)
                        if(ppo!=null && !ppo.isEmpty()){
                            view_pager_img_show.currentItem = ppo.toInt()
                        }
                    }


                }
                else{

                    flagSelect = 0

                    indexOfSize = 0


                    defultSelection(prodact, stack, dolar,userLoc)

                    colorSeelectionGet(prodact,stack,dolar,userLoc)

                    show_prod_hasmuch.text = prodact.contn.get(stack).toString()
                    if (prodact.contn.get(selectSize).toString().toInt() >15){
                        l_Totoale.visibility = View.GONE
                    }
                    else{
                        l_Totoale.visibility = View.VISIBLE
                    }

                }
            }
        }



    }








    fun colorSeelectionGet(prodact:Prodacts,selectSize: String,dolar: String?, userLoc: String?){
        var colors = ArrayList<String>()

        ChipGroupColor_.removeAllViews()

        var sc:ArrayList<String>?
        if(selectSize == ".." || selectSize == "." || selectSize == "--" || selectSize == "-"){
//                        show_size_parant.visibility = View.GONE
            val scel = prodact.sizes[0].keys.first()
            sc = prodact.colors.get(scel)

        }
        else{
            sc = prodact.colors.get(selectSize)
//                        show_size_parant.visibility = View.VISIBLE

        }

        if(sc!=null && !sc.isEmpty()) {
            sc!!.forEach { sizcol ->
                colors.add(sizcol)
            }

//                        show_color_parant.visibility = View.VISIBLE
        }
        else{
            colors.add("لون واحد")
//                        show_color_parant.visibility = View.GONE

        }

        if (colors.size == 1 && colors[0]=="لون واحد"){
            flagColorSelection = 1
        }


        for (color in colors) {
            val chip = createChip(color)
            ChipGroupColor_.addView(chip)

            for (i in 0 until ChipGroupColor_.childCount) {
                val chip = ChipGroupColor_.getChildAt(i) as Chip
                chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if (isChecked) {
                        defultSelection(prodact, selectSize, dolar,userLoc)
                        if(!prodact.imgColor.isEmpty()){
                            val ppo =  prodact.imgColor.get(show_color_prodacts.selectedItem.toString())
                            if(ppo!=null && !ppo.isEmpty()){
                                view_pager_img_show.currentItem = ppo.toInt()
                            }
                        }
                        indexOfColor = i

                    }
                    else{
                        indexOfColor = 0
                    }
                }
            }

        }


    }

    fun createChip(size: String): Chip {
        val chip = Chip(this)
        chip.text = size
        chip.isClickable = true
        chip.isCheckable = true
        return chip
    }
    private fun displayProdactItem(
        prodItem: Prodacts,
        sizes: ArrayList<String>,
        prisecs: ArrayList<String>
    ) {
        show_prod_name.text = prodItem.productName
        show_prod_about.text = prodItem.about


        ImageViewPigar().mainImgPager(
            prodItem.imageLinks,
            worm_dots_indicator,
            view_pager_img_show,
            this,
            this,null
            ,showDialog,imgViewShowALl
        )


        var Size_price = prodItem.sizes

        Size_price.forEach { ma->
            ma.keys.forEach {ss->
                sizes.add(ss.toString())

            }
            ma.values.forEach {vv->
                prisecs.add(vv)
            }

        }





        var adapter = ArrayAdapter(this,R.layout.spiner_layoyt, sizes)
        adapter.setDropDownViewResource(R.layout.siper2);
        show_sizes_prodacts.adapter = adapter
    }

    private fun addToCallt(prodactId:String,state:String,countMach:Prodacts){
        btn_add_to_bag.setOnClickListener {
            takeProdact(prodactId, countMach, null)

        }
            btn_buy_now.setOnClickListener {
                takeProdact(prodactId, countMach, "ByNow")
            }












    }

    private fun takeProdact(prodactId: String, countMach: Prodacts,buyNow:String?){

        prog_add_to_cart.visibility = View.VISIBLE
        ChickerFunction().chickUser(this) { userDate ->

            if(userDate!=null){
                
                var userId = UserServes().getUserID()
                if (flagSelect ==1 && flagColorSelection == 1) {

                    var chipSize=  ChipGroupSize_.getChildAt(indexOfSize) as Chip
                    var chipColor=  ChipGroupColor_.getChildAt(indexOfColor) as Chip

                    var color = chipColor.text.toString()
                    var size =chipSize.text.toString()
                    var mmu = order_mach.text.toString()
                    if (buyNow==null){
                        GetSalah().getSalahDoc(userId){clat ->
                            if (clat!=null){
                                var flag = 0
                                var newClat = clat
                                if (newClat.state == userDate.user.state){

                                    for (cl_prod_id in newClat.products) {
                                        if (cl_prod_id.prodid == prodactId && cl_prod_id.sizeprice.containsKey(size) && cl_prod_id.color == color) {
                                            if (countMach.contn[size] != null && countMach.contn[size].toString().toInt() > 0) {
                                                var newTotal = cl_prod_id.much.toInt() + mmu.toInt()
                                                if (newTotal <= countMach.contn[size].toString().toInt()) {

                                                    cl_prod_id.much = newTotal.toString()

                                                    GetSalah().setSalahDoc(userId,newClat){
                                                        Snackbar.make(count_add, "تمت الاضافة الى السلة بنجاح", Snackbar.LENGTH_LONG).show()
                                                        prog_add_to_cart.visibility = View.GONE
                                                        GetSalah().salahCount(sala_count_show)

                                                    }


                                                }
                                                else {
                                                    Snackbar.make(count_add, "قمت بحجز كل الكمية المتوفرة", Snackbar.LENGTH_LONG).show()
                                                    prog_add_to_cart.visibility = View.GONE

                                                }


                                            }
                                            else {
                                                Snackbar.make(btn_add_to_bag, "المقاس موقف الرجاء التواصل مع الادارة", Snackbar.LENGTH_SHORT).show()
                                                prog_add_to_cart.visibility = View.GONE
                                            }
                                            flag = 1
                                        }

                                    }

                                    if (flag == 1){

                                    }
                                    else{
                                        if (countMach.contn[size] != null && countMach.contn[size].toString().toInt() > 0) {
                                            var price = finalStatic
                                            var mmu = order_mach.text.toString()
                                            var size_Price = HashMap<String, String>()

                                            size_Price.put(size, price)
                                            val proditem = ClatProdact(prodactId, color, mmu, size_Price)

                                            newClat.products.add(proditem)

                                            GetSalah().setSalahDoc(userId,newClat){
                                                Snackbar.make(count_add, "تمت الاضافة الى السلة بنجاح", Snackbar.LENGTH_LONG).show()
                                                prog_add_to_cart.visibility = View.GONE
                                                GetSalah().salahCount(sala_count_show)

                                            }

                                        } else {
                                            Snackbar.make(btn_add_to_bag, "المقاس موقف الرجاء التواصل مع الادارة", Snackbar.LENGTH_SHORT).show()
                                            prog_add_to_cart.visibility = View.GONE
                                        }

                                    }

                                }
                                else{
                                    Toast.makeText(this, "لقد قمت بتغير منطقة العملة.. قم بحذف جميع المنتجات من السلة ثم عاود المحاولة .. او قم بارجاع منطقة العملة من ملفك الشخصي", Toast.LENGTH_LONG).show()
                                    prog_add_to_cart.visibility = View.GONE
                                }

                            }
                            else{
                                addNewCalt(countMach,prodactId, userDate, userId,null)
                            }





                        }

                    }
                    else{
                        addNewCalt(countMach,prodactId,userDate,userId,"byNow")

                    }


                }
                else{
                    Snackbar.make(order_mach," يجب اختيار مقاس اول او لونً",Snackbar.LENGTH_SHORT).show()
                    prog_add_to_cart.visibility = View.GONE
                }




            
            }
            else{
                Snackbar.make(btn_add_to_bag, "يجب عليك تسجيل الدخول اولاً", Snackbar.LENGTH_SHORT).show()
                prog_add_to_cart.visibility = View.GONE
            }
        
            
        }

    }

    private fun addNewCalt(prodact: Prodacts,prodactId: String, userDate: UserDate, userId: String,byNow:String?) {

        prog_add_to_cart.visibility = View.VISIBLE
        var chipSize=  ChipGroupSize_.getChildAt(indexOfSize) as Chip
        var chipColor=  ChipGroupColor_.getChildAt(indexOfColor) as Chip

        var color = chipColor.text.toString()
        var size =chipSize.text.toString()

        if (prodact.contn[size] != null && prodact.contn[size].toString().toInt() > 0) {
            var price = finalStatic
            var mmu = order_mach.text.toString()


            var size_Price = HashMap<String, String>()

            size_Price.put(size, price)


            val proditem = ClatProdact(prodactId, color, mmu, size_Price)
            var prodArr = ArrayList<ClatProdact>()
            prodArr.add(proditem)

            var newClat = Clat(Calendar.getInstance().time, userDate.user.state, prodArr)


            GetSalah().setSalahDoc(userId, newClat) {

                if (byNow!=null){
                    prog_add_to_cart.visibility = View.GONE
                    var intent = Intent(this,ComliteorderActivity::class.java)
                    intent.putExtra("totalPrice",finalStatic)
                    startActivity(intent)
                }
                else{
                    Snackbar.make(count_add, "تمت الاضافة الى السلة بنجاح", Snackbar.LENGTH_LONG).show()
                    prog_add_to_cart.visibility = View.GONE
                    GetSalah().salahCount(sala_count_show)
                }


            }


        } else {
            Snackbar.make(btn_add_to_bag, "المقاس موقف الرجاء التواصل مع الادارة", Snackbar.LENGTH_SHORT).show()
            prog_add_to_cart.visibility = View.GONE
        }
    }



    private fun adminHiddenShowProdact(prodItem: Prodacts, firestore: FirebaseFirestore, prodactId: String?) {
        btn_prod_state.setOnClickListener {
            if (prodItem.state == "1")
                r_show.isChecked = true
            else
                r_hidden.isChecked = false

            dialog2.show()
        }

        btn_hidn_conferm.setOnClickListener {
            var newState = ""
            if (r_show.isChecked)
                newState = "1"
            else
                newState = "0"
            prog_hidin.visibility = View.VISIBLE
            //var newProda = Prodacts(prodItem.productName,prodItem.about,Calendar.getInstance().time,prodItem.category,prodItem.sizes,prodItem.imageLinks,prodItem.colors,prodItem.contn,prodItem.discount,newState)
            firestore.collection("prodacts2").document(prodactId!!).update("state", newState)
                .addOnSuccessListener {
                    Toast.makeText(this, "تم التحديث بنجاح", Toast.LENGTH_SHORT).show()
                    prog_hidin.visibility = View.GONE
                    dialog2.cancel()
                }
        }
    }

    private fun colorAndSizeSelectChing(prodItem: Prodacts, dolar: String?,userLoc: String?) {


        show_sizes_prodacts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    indexOfSize = position
                    var colors = ArrayList<String>()

                    var selectSize = show_sizes_prodacts.selectedItem.toString()


                    defultSelection(prodItem, selectSize, dolar,userLoc)


                   if(!prodItem.imgSize.isEmpty()){
                      val ppo =  prodItem.imgSize.get(selectSize)
                       if(ppo!=null && !ppo.isEmpty()){
                           view_pager_img_show.currentItem = ppo.toInt()
                       }
                   }

                    var sc:ArrayList<String>?
                    if(selectSize == ".." || selectSize == "." || selectSize == "--" || selectSize == "-"){
//                        show_size_parant.visibility = View.GONE
                        val scel = prodItem.sizes[0].keys.first()
                         sc = prodItem.colors.get(scel)

                    }
                    else{
                        sc = prodItem.colors.get(selectSize)
//                        show_size_parant.visibility = View.VISIBLE

                    }


                    if(sc!=null && !sc.isEmpty()) {
                        sc!!.forEach { sizcol ->
                            colors.add(sizcol)
                        }

//                        show_color_parant.visibility = View.VISIBLE
                    }
                    else{
                        colors.add("لون واحد")
//                        show_color_parant.visibility = View.GONE

                    }
                    var adapter = ArrayAdapter(this@ShowActivity,R.layout.spiner_layoyt, colors)
                    adapter.setDropDownViewResource(R.layout.siper2)
                    show_color_prodacts.adapter = adapter
                    show_color_prodacts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                defultSelection(prodItem, selectSize, dolar,userLoc)
                                if(!prodItem.imgColor.isEmpty()){
                                    val ppo =  prodItem.imgColor.get(show_color_prodacts.selectedItem.toString())
                                    if(ppo!=null && !ppo.isEmpty()){
                                        view_pager_img_show.currentItem = ppo.toInt()
                                    }
                                }

                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                        }

                    show_prod_hasmuch.text = prodItem.contn.get(selectSize).toString()






                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }


    }

    private fun defultSelection(prodItem: Prodacts, selectSize: String, dolar: String?,userLoc: String?) {

        if(userLoc =="جنوب"){
            if(prodItem.downprice.isNotEmpty()){
                val mm = decimalFormat.format(prodItem.downprice.get(selectSize).toString().toDouble() * dolar!!.toString()
                    .toDouble())
                price = mm!!
                show_prod_price.text = price
                finalStatic = price
                total_cost.text = price
                tezt_show_total.text = price
                order_mach.text = "1"
                show_prod_decount.text=""

            }
            else{
                showNprmalPrice(prodItem, selectSize, dolar)
            }

        }
        else{
            showNprmalPrice(prodItem, selectSize, dolar)
        }



    }

    private fun showNprmalPrice(
        prodItem: Prodacts,
        selectSize: String,
        dolar: String?
    ) {


        var selectPrice = prodItem.sizes[indexOfSize].get(selectSize).toString().toDouble()
        var pp= selectPrice * dolar.toString().toDouble()
        val mm = decimalFormat.format(
            selectPrice * dolar!!.toString()
                .toDouble()
        )
        price = mm!!



        if (!prodItem.discount.isEmpty() && prodItem.discount.get(selectSize) != null) {

            if(prodItem.expirDesc!=null){
                if(currentDate < prodItem.expirDesc){

                    val priceWithStrikeThrough = SpannableString(price + "ريال ")
                    priceWithStrikeThrough.setSpan(
                        StrikethroughSpan(),
                        0,
                        priceWithStrikeThrough.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val mn = decimalFormat.format(
                        prodItem.discount.get(selectSize).toString().toDouble() * dolar!!.toDouble()
                    )
                  descount = mn!!


                    show_prod_price.text = priceWithStrikeThrough
                    total_cost.text = descount
                    tezt_show_total.text = descount
                    show_prod_decount.text = descount
                    finalStatic = descount
                    order_mach.text = "1"


                    var pss= mm.toString().toDouble()-mn.toString().toDouble()
                   prcet = ( (pss / mm.toString().toDouble()) * 100).toInt()


                    show_prod_prcent.visibility = View.VISIBLE
                    show_prod_prcent.text =  "خصم : " + prcet.toString()+"%"


                    val timer = Timer()
                    var expirationDate = prodItem.expirDesc
                    timer.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            val diffInMillis = expirationDate!!.time - Calendar.getInstance().timeInMillis

                            val seconds = (diffInMillis / 1000) % 60
                            val minutes = (diffInMillis / (1000 * 60)) % 60
                            val hours = (diffInMillis / (1000 * 60 * 60)) % 24
                            val days = (diffInMillis / (1000 * 60 * 60 * 24))

                            runOnUiThread {
                                timer_end_text.text = String.format("%02d يوم , %02d:%02d:%02d", days, hours, minutes, seconds)
                            }

                            if (diffInMillis <= 0) {
                                timer.cancel()
                            }
                        }
                    }, 0, 1000)


                }


                else{
                    show_prod_price.text = price
                    finalStatic = price
                    total_cost.text = price
                    tezt_show_total.text =price
                    order_mach.text = "1"
                    show_prod_decount.text = ""

                    timer_end_text.text="انتهت مدة التخفيض"
                }

            }
            else{
                val priceWithStrikeThrough = SpannableString(price + "ريال ")
                priceWithStrikeThrough.setSpan(
                    StrikethroughSpan(),
                    0,
                    priceWithStrikeThrough.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val mn = decimalFormat.format(
                    prodItem.discount.get(selectSize).toString().toDouble() * dolar!!.toDouble()
                )
                descount = mn!!

                show_prod_price.text = priceWithStrikeThrough
                total_cost.text = descount
                tezt_show_total.text =descount
                show_prod_decount.text = descount
                finalStatic = descount
                order_mach.text = "1"
                var pss= mm.toString().toDouble()-mn.toString().toDouble()
                prcet = ( (pss / mm.toString().toDouble()) * 100).toInt()
                show_prod_prcent.visibility = View.VISIBLE
                show_prod_prcent.text =  "خصم : " + prcet.toString()+"%"



            }


        }
        else {

            show_prod_price.text = price
            finalStatic = price
            total_cost.text = price
            tezt_show_total.text = price
            order_mach.text = "1"
            show_prod_decount.text = ""
            show_prod_prcent.visibility = View.GONE
            timer_end_text.visibility = View.GONE
        }
    }

    private fun addFunction(){

        val mnd = decimalFormat.format(finalStatic.toDouble() * order_mach.text.toString().toDouble())
        var total = mnd!!
        total_cost.text = total
        tezt_show_total.text = total
    }


    private fun countAddMinass(){



        count_add.setOnClickListener {
            var hasmuch = show_prod_hasmuch.text.toString().toInt()
            if(order_mach.text.toString().toInt() < hasmuch ) {
                order_mach.text = (order_mach.text.toString().toInt() + 1).toString()
                addFunction()
            }
            else{
                Toast.makeText(this,"لقد نفذت كمية هذا المنتج حالياً .. سيتم توفيره في اقرب وقت",Toast.LENGTH_SHORT).show()
            }
        }

        count_minas.setOnClickListener {
            var ordm = order_mach.text.toString().toInt()

            if(ordm>1){
                order_mach.text = (order_mach.text.toString().toInt() - 1).toString()
                addFunction()
            }


        }


    }


    private fun chick(){
        if(FirebaseAuth.getInstance().currentUser!=null){
            val adminId = FirebaseAuth.getInstance().currentUser!!.uid
            if(adminId == "9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || adminId == "VfEvVuiJTdbCep7fQcYQVG6MivH3"){
                admin_prodactMa.visibility = View.VISIBLE

            }
        }

    }

    private fun imelmnt() {


        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_delete_conferm)

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)


        btn_delete_cansel = dialog.findViewById(R.id.btn_delete_cansel)
		btn_delete_conferm = dialog.findViewById(R.id.btn_delete_conferm)
        prog_delete = dialog.findViewById(R.id.prog_delete)

        dialog2 = Dialog(this)
        dialog2.setContentView(R.layout.dialog_prodact_hiddin)

        dialog2.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog2.window?.attributes?.windowAnimations = R.style.DialogAnimation

        show_prod_prcent = findViewById(R.id.show_prod_prcent)
        dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.window?.setGravity(Gravity.BOTTOM)

		r_show = dialog2.findViewById(R.id.r_show)
		r_hidden = dialog2.findViewById(R.id.r_hidden)
		prog_hidin = dialog2.findViewById(R.id.prog_hidin)
		R_prod_hid_show = dialog2.findViewById(R.id.R_prod_hid_show)
		btn_hidn_cansel = dialog2.findViewById(R.id.btn_hidn_cansel)
		btn_hidn_conferm = dialog2.findViewById(R.id.btn_hidn_conferm)

//
//        show_size_parant = findViewById(R.id.show_size_parant)
//        show_color_parant = findViewById(R.id.show_color_parant)
//


        stringa = findViewById(R.id.stringa)
        bakc_show = findViewById(R.id.bakc_show)
        toolbar_tital = findViewById(R.id.toolbar_tital)
        share_prodact = findViewById(R.id.share_prodact)
        hart_icon_show = findViewById(R.id.hart_icon_show)
        sala_count_show = findViewById(R.id.sala_count_show)
        salah_short_show = findViewById(R.id.salah_short_show)

        l_size = findViewById(R.id.l_size)
        countss = findViewById(R.id.countss)
        txtsize = findViewById(R.id.txtsize)
        l_color = findViewById(R.id.l_color)
        l_Totoal = findViewById(R.id.l_Totoal)
        shim_show = findViewById(R.id.shim_show)
        sec_total = findViewById(R.id.sec_total)
        price_des = findViewById(R.id.price_des)
        count_add = findViewById(R.id.count_add)
        scrol_show = findViewById(R.id.scrol_show)
        order_mach = findViewById(R.id.order_mach)
        total_cost = findViewById(R.id.total_cost)

        count_minas = findViewById(R.id.count_minas)
        list_show_1 = findViewById(R.id.list_show_1)
        btn_buy_now = findViewById(R.id.btn_buy_now)
        section_name = findViewById(R.id.section_name)
        linearLayout4 = findViewById(R.id.linearLayout4)
        linearLayout2 = findViewById(R.id.linearLayout2)
        btn_prod_state = findViewById(R.id.btn_prod_state)
        timer_end_text = findViewById(R.id.timer_end_text)
        show_prod_name = findViewById(R.id.show_prod_name)
        ChipGroupSize_ = findViewById(R.id.ChipGroupSize_)
        btn_add_to_bag = findViewById(R.id.btn_add_to_bag)
        admin_prodactMa = findViewById(R.id.admin_prodactMa)
        btn_prod_delete = findViewById(R.id.btn_prod_delete)
        btn_prod_update = findViewById(R.id.btn_prod_update)
        show_prod_price = findViewById(R.id.show_prod_price)
        show_prod_about = findViewById(R.id.show_prod_about)
        ChipGroupColor_ = findViewById(R.id.ChipGroupColor_)
        tezt_show_total = findViewById(R.id.tezt_show_total)
        show_prod_prcent = findViewById(R.id.show_prod_prcent)
        prog_add_to_cart = findViewById(R.id.prog_add_to_cart)
        show_prod_hasmuch = findViewById(R.id.show_prod_hasmuch)
        show_prod_decount = findViewById(R.id.show_prod_decount)
        view_pager_img_show = findViewById(R.id.view_pager_img_show)
        worm_dots_indicator = findViewById(R.id.worm_dots_indicator)
        show_sizes_prodacts = findViewById(R.id.show_sizes_prodacts)
        show_color_prodacts = findViewById(R.id.show_color_prodacts)


        l_Totoale =  findViewById(R.id.l_Totoale)





        showDialog = Dialog(this)
        showDialog.setContentView(R.layout.show_image)




        showDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        showDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        showDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showDialog.window?.setGravity(Gravity.BOTTOM)



        bakc_Image = showDialog.findViewById(R.id.bakc_Image)
        tol_bar_image = showDialog.findViewById(R.id.tol_bar_image)
        imgViewShowALl = showDialog.findViewById(R.id.imgViewShowALl)





    }

}