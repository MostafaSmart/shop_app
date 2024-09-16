package com.smartherd.alameer3.activitys.adapders

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.activty.MoreActivity
import com.smartherd.alameer3.activitys.models.Section
import com.smartherd.alameer3.activitys.serves.GetProdacts
import com.smartherd.alameer3.activitys.serves.ProdactServes

class BaceListAdapter(val sectionList:ArrayList<Section>, val sectionId:ArrayList<String>,var dolar:String,var userLoc:String): RecyclerView.Adapter<BaceListAdapter.Viewha>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.bace_list_item,parent,false)



        return Viewha(v)
    }
    override fun getItemCount(): Int {
       return sectionList.size
    }
    override fun onBindViewHolder(holder: Viewha, position: Int) {

        val data = sectionList[position]
        val id = sectionId[position]

        holder.section_item_tital.text = data.name
        holder.section_item_list.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)


        ProdactServes().getProdactBySection(dolar,id,true,10){prodactItemView->
            var cc = 0
            if (prodactItemView.prodacts.size > 9)
                cc = 9
            else if (prodactItemView.prodacts.size <= 9)
                cc =  prodactItemView.prodacts.size
            holder.section_item_list.adapter = ProdactAdapter(prodactItemView.prodacts,prodactItemView.prodaID,cc,dolar,userLoc)

        }


//        GetProdacts().getAllListProdacts(holder.section_item_list,null,id,"bySec",null,null,null)


        holder.section_item_go.setOnClickListener {
            var intent = Intent(holder.itemView.context,MoreActivity::class.java)

            intent.putExtra("sectionId",id)
            holder.itemView.context.startActivity(intent)
        }


    }



    class Viewha (vim: View, var  postID:String?=null) :RecyclerView.ViewHolder(vim){

        val tit_2 = vim.findViewById<LinearLayout>(R.id.tit_2);
        val section_item_go = vim.findViewById<TextView>(R.id.section_item_go);
        val section_item_tital = vim.findViewById<TextView>(R.id.section_item_tital);
        val section_item_list = vim.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.section_item_list);

    }
}