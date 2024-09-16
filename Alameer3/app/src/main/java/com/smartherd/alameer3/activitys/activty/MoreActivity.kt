package com.smartherd.alameer3.activitys.activty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.DataStrac.ProdactItemView
import com.smartherd.alameer3.activitys.Helpers.ChickerFunction
import com.smartherd.alameer3.activitys.adapders.GraidAdapter2
import com.smartherd.alameer3.activitys.adapders.GridAdapter
import com.smartherd.alameer3.activitys.models.Favorite
import com.smartherd.alameer3.activitys.models.Prodacts
import com.smartherd.alameer3.activitys.models.Section
import com.smartherd.alameer3.activitys.models.UserDate
import com.smartherd.alameer3.activitys.serves.GetProdacts
import com.smartherd.alameer3.activitys.serves.GetSections
import com.smartherd.alameer3.activitys.serves.ProdactServes
import com.smartherd.alameer3.activitys.serves.UserServes

class MoreActivity : AppCompatActivity() {
    private lateinit var shim_more:ShimmerFrameLayout
    private lateinit var more_grid:GridView
    private lateinit var show_contener:LinearLayout
    private lateinit var more_heder:LinearLayout
    private lateinit var hedar_tital:TextView
    private lateinit var heder_bake:ImageView
    private lateinit var liner_filter: TextInputLayout
    private lateinit var serch_btn:ImageButton
    private lateinit var descount_section_felter: MaterialAutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)

        imelmnt()

        var sectId = intent.getStringExtra("sectionId")
        var descount = intent.getStringExtra("descount")
        var hedin = intent.getStringExtra("hidden")
        var fiv = intent.getStringExtra("fav")
        var all = intent.getStringExtra("allPro")

        ChickerFunction().chickUser(this) { userDate ->
            if (userDate!=null){
                var userId = UserServes().getUserID()

                if (all!=null){
                    ProdactServes().getLastProdacts(userDate.dolar,true,100){prodactItemView ->

                        SetUpgridViewItemsAndClick(prodactItemView,userDate.dolar,userDate.user.state,"المضاف مؤخرا")
                        serch_btn.visibility = View.VISIBLE
                        serchEnable(prodactItemView,userDate.dolar,userDate.user.state,"المضاف مؤخرا")

                    }
                }
                if(sectId!=null){
                    ProdactServes().getProdactBySection(userDate.dolar,sectId,false,0){prodactItemView ->

                        GetSections().getSectionByID(sectId){section ->
                            if(section!=null){
                                SetUpgridViewItemsAndClick(prodactItemView,userDate.dolar,userDate.user.state,section.name)

                                serch_btn.visibility = View.VISIBLE
                                serchEnable(prodactItemView,userDate.dolar,userDate.user.state,section.name)

                            }

                        }

                    }
                }


                if(descount!=null) {
                    ProdactServes().getDescountProdacts(userDate.dolar,false,0) { prodactItemView ->
                        SetUpgridViewItemsAndClick(prodactItemView,userDate.dolar,userDate.user.state,"العروض والتخفيضات")

                        descoundBySection(prodactItemView,userDate.dolar,userDate.user.state)





                    }

                }

                if (fiv!=null){
                    ProdactServes().getFavoriteProdact(userDate.dolar,userId){prodactItemView ->
                        if(prodactItemView!=null){
                            SetUpgridViewItemsAndClick(prodactItemView,userDate.dolar,userDate.user.state,"المفضلة")
                        }
                        else{
                            shim_more.stopShimmer()
                            // تطبيق الأنيميشن للاختفاء السلس
                            shim_more.animate()
                                .alpha(0f) // جعل الشفافية 0 (الاختفاء)
                                .setDuration(500) // مدة الأنيميشن بالمللي ثانية
                                .withEndAction {
                                    // بعد انتهاء الأنيميشن، اجعل العنصر غير مرئي
                                    shim_more.visibility = View.GONE
                                    show_contener.visibility = View.VISIBLE // إظهار البيانات المطلوبة
                                }
                                .start()
                            hedar_tital.text = "مفضلتي"
                            Snackbar.make(heder_bake,"لم تقم بالاضافة الى قائمة المفضلة",Snackbar.LENGTH_SHORT).show()
                        }

                    }
                }
                if(hedin!=null){
                    ProdactServes().getHiddinProdacts(){prodactItemView->
                        SetUpgridViewItemsAndClick(prodactItemView,userDate.dolar,userDate.user.state,"مخفية")
                    }
                }




            }
            else{
                UserServes().getUserDolar("شمال") {dolar->
                    if (all!=null){
                        ProdactServes().getLastProdacts(dolar,true,100){prodactItemView ->

                            SetUpgridViewItemsAndClick(prodactItemView,dolar,"ِشمال","المضاف مؤخرا")
                            serch_btn.visibility = View.VISIBLE
                            serchEnable(prodactItemView,dolar,"شمال","المضاف مؤخرا")

                        }
                    }
                    if(sectId!=null){
                        ProdactServes().getProdactBySection(dolar,sectId,false,0){prodactItemView ->

                            GetSections().getSectionByID(sectId){section ->
                                if(section!=null){
                                    SetUpgridViewItemsAndClick(prodactItemView,dolar,"شمال",section.name)
                                    serch_btn.visibility = View.VISIBLE
                                    serchEnable(prodactItemView,dolar,"شمال","المضاف مؤخرا")


                                }

                            }

                        }
                    }

                    if(descount!=null) {

                        ProdactServes().getDescountProdacts(dolar,false,0) { prodactItemView ->
                            SetUpgridViewItemsAndClick(prodactItemView,dolar,"شمال","العروض والتخفيضات")

                            descoundBySection(prodactItemView,dolar,"شمال")
                        }

                    }
                }
            }

        }









        heder_bake.setOnClickListener {
            finish()
        }





    }




    fun descoundBySection(prodactItemView:ProdactItemView,dolar: String,userLoc: String){
        liner_filter.visibility =View.VISIBLE
        descount_section_felter.hint="القسم"


        var sections = ArrayList<String>()
        var sectionsID = ArrayList<String>()
        sections.add("الكل")
        sectionsID.add("all")


        GetSections().getAllSections {sectionItemView ->
            sectionItemView.sections.forEach {
                sections.add(it.name)
            }
            sectionItemView.sectID.forEach {
                sectionsID.add(it)
            }

            descount_section_felter.setAdapter(ArrayAdapter(this,R.layout.siper2,sections))


            descount_section_felter.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var ss = descount_section_felter.text.toString()

                    if (ss.isNotEmpty()){

                        if (sections.contains(ss)){
                            var position= sections.indexOf(ss)
                            var sectionId = sectionsID[position]

                            if(sectionId!="all"){
                                shim_more.startShimmer()
                                shim_more.visibility = View.VISIBLE
                                show_contener.visibility = View.GONE


                                ProdactServes().getDescountProdactsBySection(dolar,sectionId){prodactItemView ->
                                    SetUpgridViewItemsAndClick(prodactItemView,dolar,userLoc,"العروض والتخفيضات")
                                }

                            }
                            else{
                                SetUpgridViewItemsAndClick(prodactItemView,dolar,userLoc,"العروض والتخفيضات")

                            }

                        }
                        else{

                            Toast.makeText(this@MoreActivity,"لا يوجد عروض في هذا القسم حالياً",Toast.LENGTH_SHORT).show()
                        }


                    }
                    else{
                        SetUpgridViewItemsAndClick(prodactItemView,dolar,userLoc,"العروض والتخفيضات")

                    }

                }

                override fun afterTextChanged(s: Editable?) {


                }

            })


        }

    }


    fun serchEnable(prodactItemView:ProdactItemView,dolar:String,userLoc:String,tital:String){

        var flag = 0

        serch_btn.setOnClickListener {

            if (flag == 0){
                var prodactsName = ArrayList<String>()
                var prdactsId = ArrayList<String>()
                var prodacts=ArrayList<Prodacts>()
                descount_section_felter.hint = "بحث"
                liner_filter.setStartIconDrawable(R.drawable.search_icon)

                prodactItemView.prodacts.forEach {
                    prodactsName.add(it.productName)
                    prodacts.add(it)
                }

                prodactItemView.prodaID.forEach {
                    prdactsId.add(it)
                }



                descount_section_felter.setAdapter(ArrayAdapter(this,R.layout.siper2,prodactsName))
                liner_filter.visibility =View.VISIBLE


                descount_section_felter.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        var ss = descount_section_felter.text.toString()

                        if (ss.isNotEmpty()){

                            if (prodactsName.contains(ss)){
                                var position = prodactsName.indexOf(ss)
                                var prod = prodacts[position]
                                var prID = prdactsId[position]

                                var prdItem = ProdactItemView(arrayListOf(prod), arrayListOf(prID))
                                SetUpgridViewItemsAndClick(prdItem,dolar,userLoc,tital)



                            }

                        }
                        else{
                            SetUpgridViewItemsAndClick(prodactItemView,dolar,userLoc,tital)
                        }

                    }

                    override fun afterTextChanged(s: Editable?) {



                    }

                })

                flag =1
            }
            else{
                SetUpgridViewItemsAndClick(prodactItemView,dolar,userLoc,tital)

                flag = 0
                liner_filter.visibility =View.GONE
            }

        }

    }

    fun SetUpgridViewItemsAndClick(prodactItemView:ProdactItemView,dolar:String,userLoc:String,tital:String){
        more_grid.adapter = GraidAdapter2(prodactItemView,prodactItemView.prodacts.size,dolar,userLoc,this)


        shim_more.stopShimmer()
        // تطبيق الأنيميشن للاختفاء السلس
        shim_more.animate()
            .alpha(0f) // جعل الشفافية 0 (الاختفاء)
            .setDuration(500) // مدة الأنيميشن بالمللي ثانية
            .withEndAction {
                // بعد انتهاء الأنيميشن، اجعل العنصر غير مرئي
                shim_more.visibility = View.GONE
                show_contener.visibility = View.VISIBLE // إظهار البيانات المطلوبة
            }
            .start()



        hedar_tital.text = tital

        more_grid.onItemClickListener =
            (AdapterView.OnItemClickListener { parent, view, position, id ->
                var intent = Intent(this, ShowActivity::class.java)
                intent.putExtra("prodId", prodactItemView.prodaID[position])
                intent.putExtra("dolar", dolar)
                intent.putExtra("userLoc",userLoc)
                intent.putExtra("section", prodactItemView.prodacts[position].category)
                startActivity(intent)


            })
    }


    private fun imelmnt(){
        more_grid = findViewById(R.id.more_grid)
        shim_more = findViewById(R.id.shim_more)
        show_contener = findViewById(R.id.show_contener)
        more_heder = findViewById(R.id.more_heder)
        hedar_tital = more_heder.findViewById(R.id.hedar_tital)
        heder_bake = more_heder.findViewById(R.id.heder_bake)
        descount_section_felter = findViewById(R.id.descount_section_felter)
        liner_filter = findViewById(R.id.liner_filter)

        serch_btn = findViewById(R.id.serch_btn)

    }
}