package com.smartherd.alameer3.activitys.adapders

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.activty.EditecitysActivity
import com.smartherd.alameer3.activitys.models.Citys
import com.smartherd.alameer3.activitys.models.Prodacts

class CantoryAdapter(val contotes:ArrayList<String>): RecyclerView.Adapter<CantoryAdapter.Viewha>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_contorys,parent,false)



        return Viewha(v)
    }

    override fun getItemCount(): Int {

        return contotes.size
    }

    override fun onBindViewHolder(holder: Viewha, position: Int) {
        holder.contory_name.text = contotes[position]
        holder.itemView.setOnClickListener {
            var intent = Intent(holder.itemView.context,EditecitysActivity::class.java)
            intent.putExtra("contory",contotes[position])
            holder.itemView.context.startActivity(intent)
        }

    }


    class Viewha(vim: View, var  postID:String?=null) :RecyclerView.ViewHolder(vim) {

        val contory_name = vim.findViewById<TextView>(R.id.contory_name);

    }
}