package com.smartherd.alameer3.activitys.adapders

import android.app.LauncherActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.activty.MoreActivity
import com.smartherd.alameer3.activitys.models.Section
import com.smartherd.alameer3.activitys.serves.GetProdacts
import com.squareup.picasso.Picasso

//class ListImageAdapter (val contotes:ArrayList<String>): ArrayAdapter<LauncherActivity.ListItem>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout,parent,false)
//
//
//        return Viewha(v)
//    }
//
//    override fun getItemCount(): Int {
//        return contotes.size
//    }
//    override fun onBindViewHolder(holder: Viewha, position: Int) {
//
//
//        Picasso.get().load(contotes[position]).into(holder.img_list)
//
//
//    }
//
//
//
//
//    class Viewha(vim: View, var  postID:String?=null) : RecyclerView.ViewHolder(vim) {
//        val img_list = vim.findViewById<ImageView>(R.id.img_list);
//
//    }
//
//}


//class fAdap(context: Context, img:ArrayList<String>) : ArrayAdapter<String>(context,0,img) {
//
//
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//
//        var listfrot = LayoutInflater.from(context).inflate(R.layout.list_item_layout,parent,false)
//
//        var myfros = getItem(position)
//
//
//        var phto:ImageView = listfrot.findViewById(R.id.img_list)
//
//        Picasso.get().load(myfros).into(phto)
//
//
//
//        return listfrot
//    }
//
//}

class CustomAdapterList(private val context: Context, private val items: ArrayList<Uri>) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): String {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_layout,parent,false)
        val item = items[position]

        val img_list = view.findViewById<ImageView>(R.id.img_list)
        Picasso.get().load(item.toString()).into(img_list)





        return view
    }
}


class ImgListAdapter( val imgss:ArrayList<String>): RecyclerView.Adapter<ImgListAdapter.Viewha>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout,parent,false)



        return Viewha(v)
    }
    override fun getItemCount(): Int {
        return imgss.size
    }
    override fun onBindViewHolder(holder: Viewha, position: Int) {

        val ids = imgss[position]

       Picasso.get().load(ids).into(holder.img)



    }



    class Viewha (vim: View, var  postID:String?=null) :RecyclerView.ViewHolder(vim){

        val img = vim.findViewById<ImageView>(R.id.img_list);

    }
}