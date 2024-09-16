package com.smartherd.alameer3.activitys.adapders

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.DataStrac.ProdactItemView
import com.smartherd.alameer3.activitys.MainActivity
import com.smartherd.alameer3.activitys.models.Prodacts
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


class GraidAdapter2(var prodactItemView: ProdactItemView,var countt:Int , var dolar:String,var userLoc:String,var context:Context): BaseAdapter()  {
    override fun getCount(): Int {
        return countt
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.item_test_prodact,parent,false)

        val prod2_img = view.findViewById<ImageView>(R.id.prod3_img);
        val prod2_name = view.findViewById<TextView>(R.id.prod3_name);
        val prod2_desc = view.findViewById<TextView>(R.id.prod3_desc);
        val prod2_price = view.findViewById<TextView>(R.id.prod3_price);
        val prod2_dcount = view.findViewById<TextView>(R.id.prod3_dcount);
        val  prod2_dcount_pres = view.findViewById<TextView>(R.id.prod3_dcount_pres)

        val hart_icon_show = view.findViewById<ImageView>(R.id.hart_icon_show);
        val prod_paran_d = view.findViewById<RelativeLayout>(R.id.prod_paran_d);

        val animation = TranslateAnimation(-200f, 0f, 0f, 0f)
        animation.duration = 500
        animation.fillAfter = true

        // تطبيق الانتقال (slide) للعنصر
        val data = prodactItemView.prodacts[position]


        if(data.state=="0"){
            prod2_desc.text = "منتج مخفي"
            prod2_desc.setTextColor(android.graphics.Color.GREEN)
            prod2_desc.textSize= 18.0F
        }
        else{
            if(data.about =="."){
                prod2_desc.visibility = View.GONE
            }
            else{

                prod2_desc.text = data.about
            }
        }



        prod2_name.text = data.productName
        if(data.imageLinks.size>0){
            Picasso.get().load(data.imageLinks[0]).into(prod2_img)
        }

        if(userLoc == "جنوب"){
            if(data.downprice.isNotEmpty()){
                val firstValue1 = data.downprice.values.firstOrNull()
                val decimalFormat = DecimalFormat("0.00")
                val mm = decimalFormat.format(firstValue1.toString().toDouble() * dolar.toDouble())
                var price = mm!!

                prod2_price.text = price+" ريال "
                prod2_dcount.visibility= View.GONE


            }
            else{
                showNormalPrice(data, prod2_dcount, prod2_price,prod2_dcount_pres)
            }
        }
        else{
            showNormalPrice(data, prod2_dcount, prod2_price,prod2_dcount_pres)
        }








        return view
    }


    private fun showNormalPrice(
        data: Prodacts,
        prod2_dcount: TextView,
        prod2_price: TextView,
        prod2_dcount_pres:TextView
    ) {
        val firstValue = data.sizes[0].values.firstOrNull()
        if (firstValue != null && firstValue != "") {
            try {
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
                        prod2_dcount.text = descount + " ريال "
                        prod2_price.text = priceWithStrikeThrough
                        prod2_dcount_pres.visibility = View.VISIBLE
                        var ll = price.toDouble() - descount.toDouble()
                        var ss =  ((ll / price.toDouble()) * 100).toInt()
                        prod2_dcount_pres.text = ss.toString()+" %"
                    }


                }
                else {
                    prod2_price.text = price + " ريال "
                    prod2_dcount.visibility = View.GONE

                }
            }
            catch (e:Exception){
                Toast.makeText(context,"حدث خطاء", Toast.LENGTH_SHORT).show()
                var intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
            }

            // اضهار التخفيض اذا كان موجود

        }
    }

}