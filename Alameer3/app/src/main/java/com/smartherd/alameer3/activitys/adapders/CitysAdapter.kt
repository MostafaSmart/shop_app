package com.smartherd.alameer3.activitys.adapders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.smartherd.alameer3.R

class CitysAdapter(val contotes:ArrayList<String>): RecyclerView.Adapter<CitysAdapter.Viewha>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_edittext,parent,false)


        return Viewha(v)
    }

    override fun getItemCount(): Int {
       return contotes.size
    }
    override fun onBindViewHolder(holder: Viewha, position: Int) {
       holder.item_editText.setText(contotes[position])
    }




    class Viewha(vim: View, var  postID:String?=null) :RecyclerView.ViewHolder(vim) {
        val item_editText = vim.findViewById<EditText>(R.id.item_editText);

    }

}