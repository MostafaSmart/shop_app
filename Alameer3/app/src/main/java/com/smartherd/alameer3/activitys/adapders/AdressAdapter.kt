package com.smartherd.alameer3.activitys.adapders

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.activty.OrderDitalsActivity
import com.smartherd.alameer3.activitys.models.Address
import com.smartherd.alameer3.activitys.models.Orders

class AdressAdapter(var dialog:Dialog,val AdressList:ArrayList<Address>,val AdreId:ArrayList<String>,var selctedAddrs:HashMap<String,String>,var flag:Int): RecyclerView.Adapter<AdressAdapter.Viewha>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_address,parent,false)


        return Viewha(v)
    }
    override fun getItemCount(): Int {
        return AdressList.count()
    }

    override fun onBindViewHolder(holder: Viewha, position: Int) {
     var  add = AdressList[position]
        var id = AdreId[position]


        holder.show_adres_name.text = add.name
        holder.show_adres1.text = add.fulladdress
        holder.show_adres_2.text = add.contry
        holder.show_adres_3.text = add.city
        holder.show_adres_4.text = add.phone

        holder.show_all_adress.setOnClickListener {

            selctedAddrs.put("name", add.name)
            selctedAddrs.put("contry", add.contry)
            selctedAddrs.put("city",  add.city)
            selctedAddrs.put("fulladdress",  add.fulladdress)
            selctedAddrs.put("phone", add.phone)
            selctedAddrs.put("delevr_coust",add.delevr_coust)
            flag = 1

            notifyDataSetChanged()
            dialog.cancel()

        }

    }



    class Viewha(vim: View, var  postID:String?=null) : RecyclerView.ViewHolder(vim)  {

        val show_adres_name = vim.findViewById<TextView>(R.id.show_adres_name);

        val show_adres1 = vim.findViewById<TextView>(R.id.show_adres1);
        val show_adres_2 = vim.findViewById<TextView>(R.id.show_adres_2);
        val show_adres_3 = vim.findViewById<TextView>(R.id.show_adres_3);
        val show_adres_4 = vim.findViewById<TextView>(R.id.show_adres_4);
        val show_all_adress = vim.findViewById<LinearLayout>(R.id.show_all_adress);

    }
}
