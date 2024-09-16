package com.smartherd.alameer3.activitys.fragmints

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.Helpers.ChickerFunction
import com.smartherd.alameer3.activitys.Helpers.FragmentChangeListener
import com.smartherd.alameer3.activitys.Helpers.FragmintChing
import com.smartherd.alameer3.activitys.MainActivity
import com.smartherd.alameer3.activitys.activty.LoginSiginpActivity
import com.smartherd.alameer3.activitys.activty.MoreActivity
import com.smartherd.alameer3.activitys.activty.PhoneTowActivity
import com.smartherd.alameer3.activitys.activty.ShowActivity
import com.smartherd.alameer3.activitys.adapders.BaceListAdapter
import com.smartherd.alameer3.activitys.adapders.GraidAdapter2
import com.smartherd.alameer3.activitys.adapders.ProdactAdapter
import com.smartherd.alameer3.activitys.models.Banals
import com.smartherd.alameer3.activitys.models.Section
import com.smartherd.alameer3.activitys.models.UserDate
import com.smartherd.alameer3.activitys.serves.GetProdacts
import com.smartherd.alameer3.activitys.serves.GetSalah
import com.smartherd.alameer3.activitys.serves.GetSections
import com.smartherd.alameer3.activitys.serves.ProdactServes
import com.smartherd.alameer3.activitys.serves.UserServes
import com.squareup.picasso.Picasso
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BaceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BaceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var fragmentChangeListener: FragmentChangeListener? = null




    private lateinit var short_1: LinearLayout
    private lateinit var short_2: LinearLayout
    private lateinit var short_3: LinearLayout

    private lateinit var main_conter: NestedScrollView
    private lateinit var sala_count_bace: TextView


    private lateinit var show_loging:ShimmerFrameLayout
    private lateinit var view_pager_img:ViewPager


    private lateinit var list_item_bysection:RecyclerView


    private lateinit var salah_short_bac: LinearLayout
    private lateinit var serch_bace: com.google.android.material.textfield.TextInputEditText
    private lateinit var handler: Handler
    private lateinit var worm_dots_indicator_main: WormDotsIndicator

    private lateinit var grid_base1:GridView
    private lateinit var short_4:LinearLayout
    private lateinit var more_descount:TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            fragmentChangeListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_bace, container, false)


        imlmnt(view)


        list_item_bysection.layoutManager = LinearLayoutManager(activity)



        ChickerFunction().chickUser(view.context){ userDate ->
            if(userDate!=null){
                getDiscounts(userDate.dolar,userDate.user.state, view)



                GetSections().getAllSections {sect->
                    list_item_bysection.adapter = BaceListAdapter(sect.sections,sect.sectID,userDate.dolar,userDate.user.state)

                }

                getBanalImegs(userDate.user.state,userDate.dolar)

                short_4.setOnClickListener {

                    fragmentChangeListener?.onFragmentChange(AdmingetOrdersFragment())

                }
            }
            else{
                UserServes().getUserDolar("شمال") { dolar ->

                    getDiscounts(dolar,"شمال",view)




                    GetSections().getAllSections {sect->
                        list_item_bysection.adapter = BaceListAdapter(sect.sections,sect.sectID,dolar,"شمال")

                    }

                    getBanalImegs("شمال",dolar)
                }

                short_4.setOnClickListener {
                    Toast.makeText(view.context,". يجب تسجيل الدخول اولاً", Toast.LENGTH_SHORT).show()


                }
            }

        }


        GetSalah().salahCount(sala_count_bace)





        clickLisiners(view)





        return view
    }


    private fun getDiscounts(dolar: String,userLoc:String, view: View) {
        ProdactServes().getDescountProdacts(dolar,true,5) { prodactItemView ->
            grid_base1.adapter = GraidAdapter2(prodactItemView, 4, dolar, userLoc, view.context)


            grid_base1.onItemClickListener =
                (AdapterView.OnItemClickListener { parent, view, position, id ->
                    var intent = Intent(view.context, ShowActivity::class.java)
                    intent.putExtra("prodId", prodactItemView.prodaID[position])
                    intent.putExtra("dolar", dolar)
                    intent.putExtra("userLoc", userLoc)
                    intent.putExtra("section", prodactItemView.prodacts[position].category)
                    startActivity(intent)


                })

            show_loging.stopShimmer()
            // تطبيق الأنيميشن للاختفاء السلس
            show_loging.animate()
                .alpha(0f) // جعل الشفافية 0 (الاختفاء)
                .setDuration(500) // مدة الأنيميشن بالمللي ثانية
                .withEndAction {
                    // بعد انتهاء الأنيميشن، اجعل العنصر غير مرئي
                    show_loging.visibility = View.GONE
                    main_conter.visibility = View.VISIBLE // إظهار البيانات المطلوبة
                }
                .start()
        }
    }


    fun clickLisiners(view: View){
        short_1.setOnClickListener {
            fragmentChangeListener?.onFragmentChange(SectionsFragment())
        }
        short_3.setOnClickListener {
            var intent = Intent(view.context,MoreActivity::class.java)
            intent.putExtra("descount","true")
            startActivity(intent)
        }
        more_descount.setOnClickListener {
            var intent = Intent(view.context,MoreActivity::class.java)
            intent.putExtra("descount","true")
            startActivity(intent)
        }



        short_2.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser!=null) {
                var intent = Intent(view.context, MoreActivity::class.java)
                intent.putExtra("fav", "true")
                startActivity(intent)
            }
            else{
                Snackbar.make(short_3,"يرجى تسجيل الدخول اولا! ّ",Snackbar.LENGTH_SHORT).show()
            }
        }
        salah_short_bac.setOnClickListener {
            fragmentChangeListener?.onFragmentChange(SalahFragment())

        }
      serch_bace.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
          if(hasFocus){
              fragmentChangeListener?.onFragmentChange(SaerchFragment())
          }
      }





    }


    private fun getBanalImegs(userLoc:String,dolar:String){

        var dp = FirebaseFirestore.getInstance().collection("panals").document("n4gMxzXo88sJFcEdHo1k")
        dp.get().addOnSuccessListener {
            val getcors = it.toObject(Banals::class.java)!!

            if(getcors.imgurl.size >= 0){
                mainImgPager(getcors.imgurl,userLoc,dolar)
            }

        }

    }
    private fun mainImgPager(images:ArrayList<HashMap<String,String>>,userLoc: String,dolar: String) {

        handler = Handler()


        view_pager_img.adapter = object : PagerAdapter() {
            override fun getCount() = images.size

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val imageView = ImageView(context)
                Picasso.get().load(images[position]["img"].toString()).into(imageView)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                container.addView(imageView)
                imageView.setOnClickListener {
                    if(images[position]["name"].toString() !="nun"){
                        var cart = ""
                        val intent = Intent(context, ShowActivity::class.java)
                        var dp = FirebaseFirestore.getInstance().collection("prodacts2")
                        dp.document(images[position]["name"].toString()).get().addOnSuccessListener { doc->
                           cart= doc.get("category").toString()
                            intent.putExtra("prodId", images[position]["name"].toString())
                            intent.putExtra("dolar", dolar)
                            intent.putExtra("userLoc", userLoc)
                            intent.putExtra("section", cart)


                            context!!.startActivity(intent)
                        }


                    }

                }
                return imageView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
        worm_dots_indicator_main.attachTo(view_pager_img)

        val autoPager = object : Runnable {
            override fun run() {
                view_pager_img.currentItem = (view_pager_img.currentItem + 1) % images.size
                handler.postDelayed(this, 3000)
            }
        }

        handler.postDelayed(autoPager, 3000)
    }

    private fun imlmnt(view: View) {





        short_1 = view.findViewById(R.id.short_1)
        short_2 = view.findViewById(R.id.short_2)
        short_3 = view.findViewById(R.id.short_3)

        serch_bace = view.findViewById(R.id.serch_bace)
        main_conter = view.findViewById(R.id.main_conter)




        view_pager_img = view.findViewById(R.id.view_pager_img)
        salah_short_bac = view.findViewById(R.id.salah_short_bac)
        sala_count_bace = view.findViewById(R.id.sala_count_bace)


        show_loging = view.findViewById(R.id.show_loging)
        worm_dots_indicator_main =view.findViewById(R.id.worm_dots_indicator_main)
        list_item_bysection = view.findViewById(R.id.list_item_bysection)
        grid_base1 = view.findViewById(R.id.grid_base1)
        short_4 = view.findViewById(R.id.short_4)

        more_descount = view.findViewById(R.id.more_descount)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BaceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BaceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}