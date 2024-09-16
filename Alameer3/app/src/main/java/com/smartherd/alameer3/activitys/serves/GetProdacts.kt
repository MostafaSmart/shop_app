package com.smartherd.alameer3.activitys.serves

import android.content.Intent
import android.content.SyncRequest
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.smartherd.alameer3.activitys.activty.LoginSiginpActivity
import com.smartherd.alameer3.activitys.activty.MoreActivity
import com.smartherd.alameer3.activitys.activty.ShowActivity
import com.smartherd.alameer3.activitys.adapders.GridAdapter
import com.smartherd.alameer3.activitys.adapders.ProdactAdapter
import com.smartherd.alameer3.activitys.models.Prodacts
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class GetProdacts {



    private var firestore = FirebaseFirestore.getInstance()

    fun getAllListProdacts(list:RecyclerView?,list2:GridView?,sectionid: String? ,flag:String,show_loging:ShimmerFrameLayout?,main_conter:LinearLayout?,cont2:LinearLayout?){

        var  userLoca=""

        var dolar = ""


        var dolareDB = firestore.collection("admin").document("dolarV")
        dolareDB.get().addOnSuccessListener { dol ->


            if(FirebaseAuth.getInstance().currentUser!=null) {

                var userdp = firestore.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)

                userdp.get().addOnSuccessListener {
                    if (it.exists()) {
                        userLoca = it.get("state").toString()

                        if (userLoca == "شمال") {
                            dolar = dol.get("dolarup").toString()
                        } else if (userLoca == "جنوب") {
                            dolar = dol.get("dolardown").toString()
                        }

                        check(
                            flag,
                            list,
                            list2,
                            dolar,
                            sectionid,
                            show_loging,
                            main_conter,
                            userLoca,
                            cont2
                        )

                    }
                    else{
                        Toast.makeText(list!!.context,". قم بتسجيل الدخول مره اخرى", Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                        val myintint = Intent(list.context, LoginSiginpActivity::class.java)
                        myintint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        list.context.startActivity(myintint)

                    }
                }
            }
            else {
                userLoca =""
                dolar = dol.get("dolarup").toString()
                check(flag,list,list2,dolar,sectionid,show_loging,main_conter,userLoca ,cont2)

            }





        }











    }

    private fun check(flag: String,list: RecyclerView?,list2: GridView?,dolar: String,sectionid: String?,show_loging:ShimmerFrameLayout?,main_conter:LinearLayout?,userLoca:String,cu:LinearLayout?){

        when(flag){
            "dixs"->{ getLastProdacts(list!!,dolar,show_loging!!,main_conter!!,userLoca,cu)}
            "bySec"->{getProdactBySection(list2,dolar,sectionid!!,list,userLoca) }
            "last"->{getLastProdacts2(list!!,dolar,show_loging!!,main_conter!!,userLoca,cu)}

        }

    }





    fun getLastProdacts2(list:RecyclerView,dolar:String,show_loging:ShimmerFrameLayout,main_conter:LinearLayout,userLoca:String,co:LinearLayout?){

        var userid = ""
        val discount= HashMap<String, String>()
        if(FirebaseAuth.getInstance().currentUser!=null){
            userid = FirebaseAuth.getInstance().currentUser!!.uid
        }
        var allCorss = ArrayList<Prodacts>()
        var corsID = ArrayList<String>()
        var dp = firestore.collection("prodacts2").orderBy("dateAdded",Query.Direction.DESCENDING)
        dp.get().addOnSuccessListener {
            it.documents.forEach { posts->
                var data = posts.toObject(Prodacts::class.java)!!
                if(data.state == "0") {
                    if (userid == "9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || userid == "VfEvVuiJTdbCep7fQcYQVG6MivH3") {
                        allCorss.add(data)
                        corsID.add(posts.id)
                    }
                }
                else if (data.state=="1"){
                    allCorss.add(data)
                    corsID.add(posts.id)
                }





            }

            var cc = 0
            if (allCorss.size > 9)
                cc = 9
            else if (allCorss.size <= 9)
                cc =  allCorss.size

            var prodactadapter = ProdactAdapter(allCorss,corsID,cc,dolar,userLoca)


            list.adapter = prodactadapter
            show_loging.stopShimmer()
            show_loging.visibility = View.GONE
            main_conter.visibility = View.VISIBLE
        }

    }

    fun getProdactBySection(list: GridView?,dolar: String,sectionid:String,list2Recyc: RecyclerView?,userLoca:String){


        var prodacts=ArrayList<Prodacts>()
        var prodid = ArrayList<String>()
        var dp = firestore.collection("prodacts2").whereEqualTo("category",sectionid).orderBy("dateAdded",Query.Direction.DESCENDING)
        dp.get().addOnSuccessListener {
            var userID = ""
            if(FirebaseAuth.getInstance().currentUser!=null){
                userID = FirebaseAuth.getInstance().currentUser!!.uid
            }
            it.documents.forEach { posts->
                var data=  posts.toObject(Prodacts::class.java)!!

                if (data.state=="0"){
                    if(userID =="9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || userID =="VfEvVuiJTdbCep7fQcYQVG6MivH3"){
                        prodacts.add(data)
                        prodid.add(posts.id)
                    }
                }
                else if(data.state=="1"){
                    prodacts.add(data)
                    prodid.add(posts.id)
                }

            }
            if (list!=null){
                val gridAdapter = GridAdapter(prodacts,prodid,dolar,list.context,userLoca)
                list.adapter = gridAdapter
                list.onItemClickListener =
                    (AdapterView.OnItemClickListener { parent, view, position, id ->

                        val postID = gridAdapter.prodId[position]
                        val dolar = gridAdapter.dolar
                        val myintint = Intent(list.context, ShowActivity::class.java)

                        myintint.putExtra("prodId", postID)
                        myintint.putExtra("dolar",dolar)
                        myintint.putExtra("userLoc",userLoca)
                        myintint.putExtra("section",prodacts[position].category)
                        list.context.startActivity(myintint)


                    })

            }
            else{

                list2Recyc!!.adapter = ProdactAdapter(prodacts,prodid,prodacts.size,dolar,userLoca)
            }




        }



    }

    fun getLastProdacts(list:RecyclerView,dolar:String,show_loging:ShimmerFrameLayout,main_conter:LinearLayout,userLoca:String,co:LinearLayout?){

        var userid = ""
        val discount= HashMap<String, String>()
        if(FirebaseAuth.getInstance().currentUser!=null){
            userid = FirebaseAuth.getInstance().currentUser!!.uid
        }
        var allCorss = ArrayList<Prodacts>()
        var corsID = ArrayList<String>()
        var dp = firestore.collection("prodacts2").whereNotEqualTo("discount",discount)
        dp.get().addOnSuccessListener {

            val sortedDocuments = it.documents.sortedByDescending { dd->

               dd.getDate("dateUpdate")

            }

            sortedDocuments.forEach { posts->
                var data = posts.toObject(Prodacts::class.java)!!
                if(data.state == "0") {
                    if (userid == "9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || userid == "VfEvVuiJTdbCep7fQcYQVG6MivH3") {
                        allCorss.add(data)
                        corsID.add(posts.id)
                    }
                }
                else if (data.state=="1"){
                    allCorss.add(data)
                    corsID.add(posts.id)
                }

            }


            var cc = 0

            if (allCorss.size > 9)
                cc = 9
            else if (allCorss.size <= 9)
                cc =  allCorss.size


            var prodactadapter = ProdactAdapter(allCorss,corsID,cc,dolar,userLoca)

            if(allCorss.size>0){
                co!!.visibility = View.VISIBLE
            }
            list.adapter = prodactadapter
            show_loging.stopShimmer()
            show_loging.visibility = View.GONE
            main_conter.visibility = View.VISIBLE
        }

    }
}

