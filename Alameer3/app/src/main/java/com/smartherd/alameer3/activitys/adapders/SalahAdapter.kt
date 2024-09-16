package com.smartherd.alameer3.activitys.adapders

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.activty.ShowActivity
import com.smartherd.alameer3.activitys.models.Clat
import com.smartherd.alameer3.activitys.models.ClatProdact
import com.smartherd.alameer3.activitys.models.Prodacts
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class SalahAdapter(var caltAll:Clat? ,var prodClatProdact: ArrayList<ClatProdact>,var dolar:String?):RecyclerView.Adapter<SalahAdapter.Viewha>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_salah,parent,false)

        val animation = TranslateAnimation(-200f, 0f, 0f, 0f)
        animation.duration = 700
        animation.fillAfter = true
        v.startAnimation(animation)




        return Viewha(v)
    }


    override fun getItemCount(): Int {
        return prodClatProdact.size
    }
//    fun removeItem(position: Int) {
//        prodClatProdact.removeAt(position)
//        notifyItemRemoved(position)
//    }

    override fun onBindViewHolder(holder: Viewha, position: Int) {

        var prodDet = prodClatProdact[position]


        var firestore = FirebaseFirestore.getInstance()

        var db= firestore.collection("prodacts2").document(prodDet.prodid)

        db.get().addOnSuccessListener {pp->
            if(pp.exists()){
                val prodactIt = pp.toObject(Prodacts::class.java)!!

                holder.salah_name.text = prodactIt.productName
                Picasso.get().load(prodactIt.imageLinks[0]).into(holder.salah_Img)


            }
            else{
                holder.salah_name.text = "منتج لم يعد متوفر"
                holder.salah_name.setTextColor(Color.RED)
            }
            if(caltAll==null){

                holder.remove_fromSalah.visibility = View.GONE
                holder.itemView.setOnClickListener {
                    if( holder.salah_name.text != "منتج لم يعد متوفر") {
                        holder.prog_sala_itm.visibility=View.VISIBLE
                        var intent = Intent(holder.itemView.context,ShowActivity::class.java)

                        var loc=""
                        var dp = firestore.collection("admin").document("dolarV")


                        firestore.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener {us->
                            loc= us.get("state").toString()

                            dp.get().addOnSuccessListener {dpl->
                                var dolar = ""
                                if(loc=="شمال"){
                                    dolar = dpl.get("dolarup").toString()
                                }
                                else if(loc=="جنوب"){
                                    dolar = dpl.get("dolardown").toString()
                                }
                                intent.putExtra("prodId",prodDet.prodid)
                                holder.prog_sala_itm.visibility=View.GONE
                                intent.putExtra("dolar",dolar)
                                intent.putExtra("section",pp.get("category").toString())
                                intent.putExtra("userLoc",us.get("state").toString())
                                holder.itemView.context.startActivity(intent)


                            }

                        }
                    }
                    else{
                        Toast.makeText(holder.itemView.context,"المنتج لم يعد متوفراً",Toast.LENGTH_SHORT).show()
                    }

                }
            }


        }

        holder.salah_color.text = prodDet.color
        var size = prodDet.sizeprice.keys.firstOrNull()
        val decimalFormat = DecimalFormat("0.00")
        val mm = decimalFormat.format(prodDet.sizeprice.get(size).toString().toDouble() * prodDet.much.toDouble())
        var price = mm!!
        holder.salah_size.text = size
        holder.salah_price.text = price
        holder.salah_mach.text = prodDet.much


        holder.remove_fromSalah.setOnClickListener {

                holder.prog_sala_itm.visibility = View.VISIBLE
                var newArray = ArrayList<ClatProdact>()
               
            
            
               
//                prodClatProdact.forEach { cop ->
//                    newArray.add(cop)
//                }

                var dp = FirebaseFirestore.getInstance().collection("calt")
            if(prodClatProdact.size>1){

                try{
                    prodClatProdact.removeAt(position)
                    notifyItemRemoved(position)
                    var clat = Clat(Calendar.getInstance().time,caltAll!!.state, prodClatProdact)

                    dp.document(FirebaseAuth.getInstance().currentUser!!.uid).set(clat)
                        .addOnSuccessListener {
                            holder.prog_sala_itm.visibility = View.GONE
                            Toast.makeText(
                                holder.itemView.context,
                                "تم الحذف . قم بتحديث السلة ",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                }
                catch (e:Exception){
                    Toast.makeText(
                        holder.itemView.context,
                        "قم بتحديث السلة و حاول مره اخرى ",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.prog_sala_itm.visibility = View.GONE
                }


            }
            else {
                try{
                    prodClatProdact.removeAt(position)
                    notifyItemRemoved(position)
                    dp.document(FirebaseAuth.getInstance().currentUser!!.uid).delete().addOnSuccessListener {

                        holder.prog_sala_itm.visibility = View.GONE
                        Toast.makeText(
                            holder.itemView.context,
                            "تم الحذف قم بتحديث السلة",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
                catch (e:Exception){
                    Toast.makeText(
                        holder.itemView.context,
                        "قم بتحديث السلة و حاول مره اخرى ",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.prog_sala_itm.visibility = View.GONE
                }


            }





        }




    }




    class Viewha(vim: View, var  postID:String?=null) :RecyclerView.ViewHolder(vim)  {

        val salah_Img = vim.findViewById<ImageView>(R.id.salah_Img);
        val salah_name = vim.findViewById<TextView>(R.id.salah_name);
        val salah_mach = vim.findViewById<TextView>(R.id.salah_mach);
        val salah_size = vim.findViewById<TextView>(R.id.salah_size);
        val salah_price = vim.findViewById<TextView>(R.id.salah_price);
        val salah_color = vim.findViewById<TextView>(R.id.salah_color);
        val prog_sala_itm = vim.findViewById<ProgressBar>(R.id.prog_sala_itm);
        val remove_fromSalah = vim.findViewById<ImageView>(R.id.remove_fromSalah);



    }


}