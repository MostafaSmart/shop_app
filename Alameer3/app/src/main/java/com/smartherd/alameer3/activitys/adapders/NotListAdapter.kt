package com.smartherd.alameer3.activitys.adapders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.models.Prodacts
import com.squareup.picasso.Picasso

class NotListAdapter(context: Context, dataArrayList: ArrayList<kotlin.collections.List<String>>) :
    ArrayAdapter<kotlin.collections.List<String>>(context, R.layout.item_salah, dataArrayList) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        var view = view
        val listData = getItem(position)

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_salah, parent, false)
        }
        val salah_Img = view!!.findViewById<ImageView>(R.id.salah_Img);
        val salah_name = view!!.findViewById<TextView>(R.id.salah_name);
        val salah_mach = view!!.findViewById<TextView>(R.id.salah_mach);
        val salah_size = view!!.findViewById<TextView>(R.id.salah_size);
        val salah_price = view!!.findViewById<TextView>(R.id.salah_price);
        val salah_color = view!!.findViewById<TextView>(R.id.salah_color);
        val prog_sala_itm = view!!.findViewById<ProgressBar>(R.id.prog_sala_itm);
        val remove_fromSalah = view!!.findViewById<ImageView>(R.id.remove_fromSalah);

        salah_name.text = listData!![1]


        remove_fromSalah.visibility = View.GONE
        if (listData[2].isNotEmpty()){
            Picasso.get().load(listData[2]).into(salah_Img)

        }

        salah_size.text = listData!![3]
        salah_price.text = listData!![4]
        salah_mach.text = listData!![5]
        salah_color.text = listData!![6]





        return view
    }
}