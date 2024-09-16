package com.smartherd.alameer3.activitys.adapders

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.DataStrac.SectionItemView
import com.smartherd.alameer3.activitys.activty.MoreActivity
import com.smartherd.alameer3.activitys.activty.ShowActivity
import com.smartherd.alameer3.activitys.models.Section

class SectionAdapder (val sectionItemView:SectionItemView): RecyclerView.Adapter<SectionAdapder.Viewha>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_section,parent,false)


        val animation = TranslateAnimation(-200f, 0f, 0f, 0f)
        animation.duration = 700
        animation.fillAfter = true
        v.startAnimation(animation)





        return Viewha(v)
    }

    override fun getItemCount(): Int {
        return sectionItemView.sections.size

    }
    override fun onBindViewHolder(holder: Viewha, position: Int) {
        try {
            val sectionDetels = sectionItemView.sections[position]

            val sectid = sectionItemView.sectID[position]




            holder.txt_sectionName.text = sectionDetels!!.name
            holder.itemView.setOnClickListener {
                var inten = Intent(holder.itemView.context, MoreActivity::class.java)
                inten.putExtra("sectionId", sectid)
                holder.itemView.context.startActivity(inten)
            }

        }
        catch (e:Exception){
            Toast.makeText(holder.itemView.context,"حدث خطاء",Toast.LENGTH_SHORT).show()
        }
    }



    class Viewha(vim: View, var  postID:String?=null) :RecyclerView.ViewHolder(vim) {
        val txt_sectionName = vim.findViewById<TextView>(R.id.txt_sectionName);

    }

}