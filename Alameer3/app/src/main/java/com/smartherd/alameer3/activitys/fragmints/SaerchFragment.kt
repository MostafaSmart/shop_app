package com.smartherd.alameer3.activitys.fragmints

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.Helpers.FragmentChangeListener
import com.smartherd.alameer3.activitys.activty.ShowActivity
import com.smartherd.alameer3.activitys.adapders.GridAdapter
import com.smartherd.alameer3.activitys.models.Prodacts

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaerchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaerchFragment : Fragment()  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var fragmentChangeListener: FragmentChangeListener? = null
    private lateinit var list_serch: GridView
    private lateinit var btn_bakc_serch: ImageView
    private lateinit var shim_serac: com.facebook.shimmer.ShimmerFrameLayout
    private lateinit var input_sarech: com.google.android.material.textfield.TextInputEditText
    private lateinit var adapter:GridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            fragmentChangeListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saerch, container, false)

        imelmnt(view)

        input_sarech.requestFocus()
        clickLisseinr()


        val firestore = FirebaseFirestore.getInstance()
        var dp = firestore.collection("prodacts2")
        var ad = firestore.collection("admin").document("dolarV")
        var dolar=""
        var userLoc=  ""
        ad.get().addOnSuccessListener {dlo->

            if(FirebaseAuth.getInstance().currentUser!=null) {
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                var us = firestore.collection("users").document(userId)
                us.get().addOnSuccessListener {use->

                    if (use.get("state").toString() =="شمال"){
                        dolar = dlo.get("dolarup").toString()
                        userLoc = "شمال"
                    }

                    else if(use.get("state").toString() =="جنوب"){
                        dolar = dlo.get("dolardown").toString()
                        userLoc = "جنوب"
                    }

                }
            }else
            {
                dolar = dlo.get("dolarup").toString()
                userLoc = " "
            }
            input_sarech.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (input_sarech.text.toString().isNotEmpty()){
                        var prodList = ArrayList<Prodacts>()
                        shim_serac.visibility = View.VISIBLE
                        list_serch.visibility = View.GONE
                        shim_serac.startShimmer()
                        var prodId = ArrayList<String>()
                        if (input_sarech.text.toString().length >= 4){
                            dp.orderBy("productName")
                                .startAt(input_sarech.text.toString().trim())
                                .endAt(input_sarech.text.toString().trim() + "\uf8ff")
                                .get().addOnSuccessListener {
                                    if(it.documents.size>0) {
                                        it.documents.forEach { posts ->
                                            prodList.add(posts.toObject(Prodacts::class.java)!!)
                                            prodId.add(posts.id)

                                        }
                                        shim_serac.stopShimmer()
                                        shim_serac.visibility = View.GONE
                                        list_serch.visibility = View.VISIBLE
                                         adapter =
                                            GridAdapter(prodList, prodId, dolar, view.context,userLoc)
                                        list_serch.adapter = adapter

                                    }
                                    else{
                                        shim_serac.stopShimmer()
                                        shim_serac.visibility = View.GONE
                                        Toast.makeText(view.context,"لم يتم العثور على نتائج",Toast.LENGTH_SHORT).show()
                                    }
                                }

                        }
                    }
                    else{
                        shim_serac.stopShimmer()
                        shim_serac.visibility = View.GONE
                        Toast.makeText(view.context,"لم يتم العثور على نتائج",Toast.LENGTH_SHORT).show()

                    }
                }

                override fun afterTextChanged(s: Editable?) {


                }
            })


        }






        return view
    }

    private fun clickLisseinr() {
        btn_bakc_serch.setOnClickListener {
            fragmentChangeListener?.onFragmentChange(BaceFragment())
        }


        list_serch.onItemClickListener =
            (AdapterView.OnItemClickListener { parent, view, position, id ->
                var intent = Intent(view.context, ShowActivity::class.java)
                intent.putExtra("prodId", adapter.prodId[position])
                intent.putExtra("dolar", adapter.dolar)
                intent.putExtra("userLoc",adapter.userLoca)
                intent.putExtra("section", adapter.prodacts[position].category)
                startActivity(intent)
            })
    }

    private fun imelmnt(view: View) {
        shim_serac = view.findViewById(R.id.shim_serac)
        list_serch = view.findViewById(R.id.list_serch)
        input_sarech = view.findViewById(R.id.input_sarech)
        btn_bakc_serch = view.findViewById(R.id.btn_bakc_serch)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaerchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaerchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}