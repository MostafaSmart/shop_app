package com.smartherd.alameer3.activitys.adapders

import android.content.Intent
import android.provider.CalendarContract.Colors
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.Color
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.activty.ShowActivity
import com.smartherd.alameer3.activitys.models.Favorite
import com.smartherd.alameer3.activitys.models.Prodacts
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class ProdactAdapter(val prodacsList:ArrayList<Prodacts>,val pro_id:ArrayList<String>,val count:Int,val dolar:String,val userLoca:String): RecyclerView.Adapter<ProdactAdapter.Viewha>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_test_prodact,parent,false)



        return Viewha(v)
    }

    override fun getItemCount(): Int {
        return count
    }
    override fun onBindViewHolder(holder: Viewha, position: Int) {
        var data = prodacsList[position]
        var prodid = pro_id[position]
        if(data.state == "0"){
            if(FirebaseAuth.getInstance().currentUser!=null ){
                var adminid = FirebaseAuth.getInstance().currentUser!!.uid
                if(adminid == "9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || adminid == "VfEvVuiJTdbCep7fQcYQVG6MivH3"){
                    holder.itemView.visibility = View.VISIBLE
                    holder.prod2_desc.text ="منتج مخفي"
                    holder.prod2_desc.setTextColor(android.graphics.Color.GREEN)
                    holder.prod2_desc.textSize= 18.0F
                }
                else{
                    holder.itemView.visibility = View.GONE

                }
            }
            else {
                holder.itemView.visibility = View.GONE
            }
        }







        holder.prod2_name.text = data.productName

        if(data.about == "."){
            holder.prod2_desc.visibility = View.GONE

        }
        else{
            holder.prod2_desc.visibility = View.VISIBLE
            holder.prod2_desc.text = data.about
        }





        if(data.imageLinks.size>0){
            Picasso.get().load(data.imageLinks[0]).into(holder.prod2_img)
        }





        if(userLoca=="جنوب"){
            if(data.downprice.isNotEmpty()){

                val firstValue1 = data.downprice.values.firstOrNull()
                val decimalFormat = DecimalFormat("0.00")

                val mm = decimalFormat.format(firstValue1.toString().toDouble() * dolar.toDouble())
                var price = mm
                holder.prod2_dcount.visibility= View.GONE
                holder.prod2_price.text = price+" ريال "


            }
            else{
                showNormalPrice(data, holder)
            }
        }
        else{
            showNormalPrice(data, holder)
        }











        holder.itemView.setOnClickListener {

        val myintint = Intent(holder.itemView.context, ShowActivity::class.java)

        myintint.putExtra("prodId",prodid)
        myintint.putExtra("dolar",dolar)
        myintint.putExtra("userLoc",userLoca)
        myintint.putExtra("section",data.category)
        holder.itemView.context.startActivity(myintint)
    }







        if(FirebaseAuth.getInstance().currentUser!=null){
            var useid = FirebaseAuth.getInstance().currentUser!!.uid
            FirebaseFirestore.getInstance().collection("favorite").document(useid).get().addOnSuccessListener {favu->
                if(favu.exists()){
                    var listFavorite= favu.toObject(Favorite::class.java)!!
                    if(listFavorite.favlist.contains(prodid)){
                        holder.hart_icon_show.setImageResource(R.drawable.hart_on)

                    }
                }

                holder.hart_icon_show.setOnClickListener {
                    var str =""
                    var listFavorite: Favorite
                    if(favu.exists()){
                        listFavorite= favu.toObject(Favorite::class.java)!!
                        if(!listFavorite.favlist.contains(prodid)){
//                            listFavorite.favlist.add(prodid)
//                            holder.hart_icon_show.setImageResource(R.drawable.hart_on)
//                            str = "تمت الاضافة الى المفضلة"

                        }
                        else{
                            listFavorite.favlist.remove(prodid)
                            holder.hart_icon_show.setImageResource(R.drawable.hart_of)
                            str = "تمت الازالة من المفضلة"

                            FirebaseFirestore.getInstance().collection("favorite").document(useid).set(listFavorite).addOnSuccessListener {
                                Toast.makeText(holder.itemView.context,str, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
//                        listFavorite =  Favorite()
//                        listFavorite.favlist.add(prodid)
//                        holder.hart_icon_show.setImageResource(R.drawable.hart_on)
//                        str = "تمت الاضافة الى المفضلة"
                    }


                }

            }
        }








    }

    private fun showNormalPrice(
        data: Prodacts,
        holder: Viewha
    ) {
        val firstValue = data.sizes[0].values.firstOrNull()
        if (firstValue != null) {
            val decimalFormat = DecimalFormat("0.00", DecimalFormatSymbols(Locale.US))

            val mm = decimalFormat.format(firstValue.toString().toDouble() * dolar.toDouble())
            var price = mm!!
            var pd = ""


            if (!data.discount.isEmpty()) {
                var keyDis = data.discount.keys.firstOrNull()

                if(keyDis!=null){
                    data.sizes.forEach {sss->
                        sss.keys.forEach { s1->
                            if (s1 == keyDis){
                                price = decimalFormat.format(sss[keyDis].toString().toDouble() * dolar.toDouble())
                            }

                        }

                    }
                }

                val firsDicount = data.discount.values.firstOrNull()
                if (firsDicount != null) {

                    val priceWithStrikeThrough = SpannableString(price + "ريال ")
                    priceWithStrikeThrough.setSpan(
                        StrikethroughSpan(),
                        0,
                        priceWithStrikeThrough.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    var mn =
                        decimalFormat.format(firsDicount.toString().toDouble() * dolar.toDouble())
                    var descount = mn!!
                    holder.prod2_dcount.visibility = View.VISIBLE
                    holder.prod2_dcount.text = descount + " ريال "
                    holder.prod2_price.text = priceWithStrikeThrough
                    holder.prod2_dcount_pres.visibility = View.VISIBLE
                    var ll = price.toDouble() - descount.toDouble()
                   var  mm = ((ll / price.toDouble()) * 100).toInt()

                    holder.prod2_dcount_pres.text = mm.toString()+" %"


                }


            } else {
                holder.prod2_dcount_pres.visibility = View.GONE
                holder.prod2_dcount.visibility = View.GONE
                holder.prod2_price.text = price + " ريال "
            }
        }
    }

    class Viewha(vim: View, var  postID:String?=null) :RecyclerView.ViewHolder(vim) {
        val prod2_img = vim.findViewById<ImageView>(R.id.prod3_img)
        val prod2_name = vim.findViewById<TextView>(R.id.prod3_name)
        val prod2_desc = vim.findViewById<TextView>(R.id.prod3_desc)
        val prod2_price = vim.findViewById<TextView>(R.id.prod3_price)
        val prod2_dcount = vim.findViewById<TextView>(R.id.prod3_dcount)

        val prod2_dcount_pres  =vim.findViewById<TextView>(R.id.prod3_dcount_pres)



        val hart_icon_show = vim.findViewById<ImageView>(R.id.hart_icon_show);
        val prod_paran_d = vim.findViewById<RelativeLayout>(R.id.prod_paran_d);




    }
}