package com.smartherd.alameer3.activitys.serves

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.activitys.adapders.SalahAdapter
import com.smartherd.alameer3.activitys.models.Clat
import com.smartherd.alameer3.activitys.models.ClatProdact
import com.smartherd.alameer3.activitys.models.UserDate
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class GetSalah {
    var dp22 = FirebaseFirestore.getInstance()
    fun getCaltProdactsFirst(totl_salah_cost: TextView,list_salah: RecyclerView, salah_loding: ShimmerFrameLayout, salah_conter: LinearLayout ,callback: () -> Unit){


        var firestore = FirebaseFirestore.getInstance()


        var  userLoca=""




        var dolareDB = firestore.collection("admin").document("dolarV")
        dolareDB.get().addOnSuccessListener { dol ->


            if(FirebaseAuth.getInstance().currentUser!=null) {
                var userdp = firestore.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)

                userdp.get().addOnSuccessListener {
                    var dolar = ""
                    userLoca = it.get("state").toString()

                    if(userLoca=="شمال"){
                        dolar = dol.get("dolarup").toString()
                    }
                    else if(userLoca=="جنوب"){
                        dolar = dol.get("dolardown").toString()
                    }
                    getSalahNow(dolar,totl_salah_cost,list_salah,salah_loding,salah_conter,userLoca){
                        callback()
                    }



                }

            }






        }








    }

    fun getSalahNow(dolar:String, totl_salah_cost: TextView, list_salah: RecyclerView, salah_loding: ShimmerFrameLayout, salah_conter: LinearLayout,userLoc:String,callback: () -> Unit){
        var calProd = ArrayList<ClatProdact>()
        var firestore = FirebaseFirestore.getInstance()
        var userId = FirebaseAuth.getInstance().currentUser!!.uid
        var totalPrice = 0.0

        val decimalFormat = DecimalFormat("0.00", DecimalFormatSymbols(Locale.US))



        var dp = firestore.collection("calt").document(userId)
        dp.get().addOnSuccessListener {
            if(it.exists()){

                val caltItem = it.toObject(Clat::class.java)!!
                var quereCount=0
                var all = caltItem.products.size

                if(caltItem.products.size>0){
                    caltItem.products.forEach { ss->
                        firestore.collection("prodacts2").document(ss.prodid).get().addOnSuccessListener { pr->
                            if(pr.exists()){
                                if(pr.get("state").toString() =="1") {
                                    calProd.add(ss)
                                    var size = ss.sizeprice.keys.firstOrNull().toString()

                                    totalPrice = totalPrice + (ss.sizeprice.get(size).toString()
                                        .toDouble() * ss.much.toDouble())
                                }

                            }
                            quereCount++
                            if(quereCount==all){
                                var salahAdapter = SalahAdapter(caltItem,calProd,dolar)
                                list_salah.adapter = salahAdapter


                                salah_loding.stopShimmer()
                                // تطبيق الأنيميشن للاختفاء السلس
                                salah_loding.animate()
                                    .alpha(0f) // جعل الشفافية 0 (الاختفاء)
                                    .setDuration(500) // مدة الأنيميشن بالمللي ثانية
                                    .withEndAction {
                                        // بعد انتهاء الأنيميشن، اجعل العنصر غير مرئي
                                        salah_loding.visibility = View.GONE
                                        salah_conter.visibility = View.VISIBLE // إظهار البيانات المطلوبة
                                    }
                                    .start()

                              var tt =  decimalFormat.format(totalPrice)
                                totl_salah_cost.text = tt.toString()
                                callback()

                            }
                        }


                    }

                }
                else{


                    salah_loding.stopShimmer()
                    // تطبيق الأنيميشن للاختفاء السلس
                    salah_loding.animate()
                        .alpha(0f) // جعل الشفافية 0 (الاختفاء)
                        .setDuration(500) // مدة الأنيميشن بالمللي ثانية
                        .withEndAction {
                            // بعد انتهاء الأنيميشن، اجعل العنصر غير مرئي
                            salah_loding.visibility = View.GONE
                            salah_conter.visibility = View.VISIBLE // إظهار البيانات المطلوبة
                        }
                        .start()
                    callback()

                }


            }
            else{


                salah_loding.stopShimmer()
                // تطبيق الأنيميشن للاختفاء السلس
                salah_loding.animate()
                    .alpha(0f) // جعل الشفافية 0 (الاختفاء)
                    .setDuration(500) // مدة الأنيميشن بالمللي ثانية
                    .withEndAction {
                        // بعد انتهاء الأنيميشن، اجعل العنصر غير مرئي
                        salah_loding.visibility = View.GONE
                        salah_conter.visibility = View.VISIBLE // إظهار البيانات المطلوبة
                    }
                    .start()
                callback()
            }
        }

    }

    fun salahCount(textCount:TextView){

        var count = 0
        if(FirebaseAuth.getInstance().currentUser!=null){
            var userid = FirebaseAuth.getInstance().currentUser!!.uid

            var dp = FirebaseFirestore.getInstance().collection("calt").document(userid)
            dp.get().addOnSuccessListener {
                if(it.exists()){
                    val caltItem = it.toObject(Clat::class.java)!!
                    caltItem.products.forEach { ss->
                        count = count+1
                    }

                    textCount.text = count.toString()
                }
                else{
                    textCount.text = count.toString()

                }


            }



        }


    }



    fun getSalahDoc(userId:String ,callback: (Clat?) -> Unit){

        dp22.collection("calt").document(userId).get().addOnSuccessListener {
            if(it.exists()){
                val caltItem = it.toObject(Clat::class.java)
                callback(caltItem)
            }
            else{
                callback(null)
            }
        }


    }

    fun setSalahDoc(userId:String ,clat:Clat,callback: (Boolean) -> Unit){

        dp22.collection("calt").document(userId).set(clat).addOnSuccessListener {
            callback(true)
        }


    }

}