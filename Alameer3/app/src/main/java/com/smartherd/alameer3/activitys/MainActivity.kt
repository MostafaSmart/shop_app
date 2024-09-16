package com.smartherd.alameer3.activitys

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.Helpers.ChickerFunction
import com.smartherd.alameer3.activitys.Helpers.FragmentChangeListener
import com.smartherd.alameer3.activitys.activty.MoreActivity
import com.smartherd.alameer3.activitys.activty.ShowActivity
import com.smartherd.alameer3.activitys.fragmints.*
import com.smartherd.alameer3.activitys.models.Prodacts
import com.smartherd.alameer3.activitys.serves.UserServes
import java.util.*

class MainActivity : AppCompatActivity(), FragmentChangeListener  {
    private lateinit var home_continer: FrameLayout
    private lateinit var dialog:Dialog
    private lateinit var fpc :FloatingActionButton
    private lateinit var bottom_nav: com.google.android.material.bottomnavigation.BottomNavigationView
    private var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        imilmint()


//        Firebase.dynamicLinks
//            .getDynamicLink(intent)
//            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
//                // Get deep link from result (may be null if no link is found)
//                var deepLink: Uri? = null
//                if (pendingDynamicLinkData != null) {
//                    deepLink = pendingDynamicLinkData.link
//                }
//
//                // Handle the deep link. For example, open the linked
//                // content, or apply promotional credit to the user's
//                // account.
//                // ...
//            }
//            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }

        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener {pendingDynamicLinkData->
            var deepLink: Uri? = null

            if (pendingDynamicLinkData!=null){
                deepLink = pendingDynamicLinkData.link
                var link = deepLink.toString().substringAfter("=")


                ChickerFunction().chickUser(this){ userDate ->
                    if(userDate!=null){
                        var  intent= Intent(this, ShowActivity::class.java)
                        intent.putExtra("prodId",link)
                        intent.putExtra("dolar",userDate.dolar)
                        intent.putExtra("userLoc",userDate.user.state)

                        startActivity(intent)
                    }
                    else{
                        UserServes().getUserDolar("شمال"){dolar->
                            var  intent= Intent(this, ShowActivity::class.java)
                            intent.putExtra("prodId",link)
                            intent.putExtra("dolar",dolar)
                            intent.putExtra("userLoc","شمال")

                            startActivity(intent)
                        }
                    }
                }
            }


        }.addOnFailureListener {e->
            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()

        }



        var clatFlag = intent.getStringExtra("clatFlage")




        if (clatFlag!=null){
            fragmntChing(SalahFragment())
        }
        else{
            fragmntChing(BaceFragment())

        }


        bottom_nav.background=null
        bottomNavIteamSelected()



    fpc.setOnClickListener {
        var intent = Intent(this, MoreActivity::class.java)
        intent.putExtra("allPro","true")
        startActivity(intent)
    }


    }

    private fun showDialog(){
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_test)


        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)

        dialog.show()
    }

    private fun bottomNavIteamSelected() {
        bottom_nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {
                    R.id.home1 -> {
                        fragmntChing(BaceFragment())
                    }
                    R.id.section -> {
                        fragmntChing(SectionsFragment())
                    }
                    R.id.your_order -> {
                        fragmntChing(SalahFragment())
                    }
                    R.id.profile -> {
                        fragmntChing(MypageFragment())
                    }

                }


                return true
            }


        })
    }

    fun fragmntChing(fragment: Fragment){
        var ching = supportFragmentManager.beginTransaction()
        ching.replace(R.id.home_continer,fragment)
        ching.commit()
        flag = false

    }

    private fun imilmint() {
        bottom_nav = findViewById(R.id.bottom_nav)
        home_continer = findViewById(R.id.home_continer)
        fpc = findViewById(R.id.fab)
    }


    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.home_continer)

        when(currentFragment){



         is SaerchFragment ->{
                fragmntChing(BaceFragment())
            }
            is AddContryFragment->{
                fragmntChing(BaceFragment())
            }

            is AdmingetOrdersFragment->{
                fragmntChing(BaceFragment())
            }
            else ->{
                if(flag){

                    super.onBackPressed()
                }
                else{
                    Toast.makeText(this,"اضغط مرة اخرى للخروج من التطبيق",Toast.LENGTH_SHORT).show()
                    flag = true
                }


            }




        }






    }

    override fun onFragmentChange(fragment: Fragment) {
        val ching = supportFragmentManager.beginTransaction()
        ching.replace(R.id.home_continer, fragment)
        ching.commit()
    }
}