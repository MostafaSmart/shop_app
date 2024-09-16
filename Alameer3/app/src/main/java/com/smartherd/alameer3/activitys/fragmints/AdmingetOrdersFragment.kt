package com.smartherd.alameer3.activitys.fragmints

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.adapders.OrderAdapter
import com.smartherd.alameer3.activitys.models.Citys
import com.smartherd.alameer3.activitys.models.Orders

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdmingetOrdersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdmingetOrdersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var showw_tii: TextView
    private lateinit var serch_order: EditText
    private lateinit var order_type: RadioGroup
    private lateinit var new_orders: RadioButton
    private lateinit var all_orders: RadioButton
    private lateinit var prog_order: ProgressBar
    private lateinit var back_admin_ordes: ImageView
    private lateinit var admin_order_list: androidx.recyclerview.widget.RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_adminget_orders, container, false)


        imelmnt(view)
        admin_order_list.layoutManager = LinearLayoutManager(activity)

        chick()

        SerchOrdersByname()





        return view
    }

    private fun SerchOrdersByname() {
        serch_order.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (serch_order.text.toString().isNotEmpty()) {
                    var firestore = FirebaseFirestore.getInstance().collection("users")
                    firestore.orderBy("name")
                        .startAt(serch_order.text.toString().trim())
                        .endAt(serch_order.text.toString().trim() + "\uf8ff").get()
                        .addOnSuccessListener {
                            if (!it.isEmpty) {
                                it.documents.forEach { us ->
                                    if (us.exists()) {
                                        getOrdersList(null, us.id)
                                    }
                                }
                            }
                        }

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun chick(){
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        if(userId == "9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || userId == "VfEvVuiJTdbCep7fQcYQVG6MivH3"){
            order_type.visibility = View.VISIBLE
            serch_order.visibility = View.VISIBLE
            getOrdersList(null,"admin")
            order_type.setOnCheckedChangeListener { group, checkedId ->

                when(checkedId){
                    R.id.all_orders->{
                        getOrdersList(null,"admin")
                    }
                    R.id.new_orders->{
                        getOrdersList("new",null)
                    }

                }

            }

        }
        else{
            getOrdersList(null,FirebaseAuth.getInstance().currentUser!!.uid)

        }

    }

    private fun getOrdersList(state:String?,user:String?) {
        var ordersList = ArrayList<Orders>()
        var orderId = ArrayList<String>()
        var dp:Query
        prog_order.visibility = View.VISIBLE
        var firestore = FirebaseFirestore.getInstance().collection("orders2")

        if(state!=null){
            dp = firestore.whereEqualTo("state",state)
        }
        else{
            if(user =="admin"){
            dp = firestore.orderBy("date", Query.Direction.DESCENDING)
            }
            else{
                dp = firestore.whereEqualTo("userId",user)
            }
        }
        dp.get().addOnSuccessListener { orders ->

            orders.documents.forEach {
                ordersList.add(it.toObject(Orders::class.java)!!)
                orderId.add(it.id)

            }
            var adapter = OrderAdapter(ordersList, orderId)
            admin_order_list.adapter = adapter
            prog_order.visibility = View.GONE

        }
    }

    private fun imelmnt(view: View) {
        showw_tii = view.findViewById(R.id.showw_tii)
        order_type = view.findViewById(R.id.order_type)
        new_orders = view.findViewById(R.id.new_orders)
        all_orders = view.findViewById(R.id.all_orders)
        prog_order = view.findViewById(R.id.prog_order)
        serch_order = view.findViewById(R.id.serch_order)
        back_admin_ordes = view.findViewById(R.id.back_admin_ordes)
        admin_order_list = view.findViewById(R.id.admin_order_list)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdmingetOrdersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdmingetOrdersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}