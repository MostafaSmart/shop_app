package com.smartherd.alameer3.activitys.adapders

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.activty.OrderDitalsActivity
import com.smartherd.alameer3.activitys.models.Clat
import com.smartherd.alameer3.activitys.models.ClatProdact
import com.smartherd.alameer3.activitys.models.Orders

class OrderAdapter(val ordersList:ArrayList<Orders>, val orderId: ArrayList<String>): RecyclerView.Adapter<OrderAdapter.Viewha>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_orders_admin,parent,false)


        return Viewha(v)
    }
    override fun getItemCount(): Int {
        return orderId.size
    }

    override fun onBindViewHolder(holder: Viewha, position: Int) {

        var data = ordersList[position]
        var id = orderId[position]

        holder.show_order_date.text = data.date.toString()
        holder.show_order_costmername.text = data.info.get("name").toString()
        holder.show_order_loction.text = data.info.get("contry").toString()
        holder.show_order_payway.text = data.info.get("pay").toString()

        when(data.state.toString()){
            "new"->{
                holder.show_order_steate.text = "جديد"
                holder.show_order_steate.setTextColor(Color.RED)
            }
            "half"->{
                holder.show_order_steate.text = "تمت المراجعة"
                holder.show_order_steate.setTextColor(Color.BLUE)
            }
        }
        holder.itemView.setOnClickListener {
            var intent = Intent(holder.itemView.context,OrderDitalsActivity::class.java)
            intent.putExtra("orderid",id)
            holder.itemView.context.startActivity(intent)
        }


    }




    class Viewha(vim: View, var  postID:String?=null) :RecyclerView.ViewHolder(vim)  {


        val show_order_date = vim.findViewById<TextView>(R.id.show_order_date);
        val show_order_payway = vim.findViewById<TextView>(R.id.show_order_payway);
        val show_order_steate = vim.findViewById<TextView>(R.id.show_order_steate);
        val show_order_loction = vim.findViewById<TextView>(R.id.show_order_loction);
        val show_order_costmername = vim.findViewById<TextView>(R.id.show_order_costmername);



    }

}