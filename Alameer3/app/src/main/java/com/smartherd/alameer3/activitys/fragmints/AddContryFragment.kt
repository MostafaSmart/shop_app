package com.smartherd.alameer3.activitys.fragmints

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.adapders.CantoryAdapter
import com.smartherd.alameer3.activitys.models.Citys

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddContryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddContryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var contory_list: androidx.recyclerview.widget.RecyclerView

    private lateinit var btn_add_contory: com.google.android.material.floatingactionbutton.FloatingActionButton

    private lateinit var dialog: Dialog
    private lateinit var btn_save_cont: Button
    private lateinit var input_contory: EditText
    private lateinit var contory_price: EditText
    private lateinit var prog_add_contory: ProgressBar


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
        val view= inflater.inflate(R.layout.fragment_add_contry, container, false)


        imelmnt(view)
        contory_list.layoutManager = LinearLayoutManager(activity)

        getConorys ()

        btn_add_contory.setOnClickListener {
            dialog.show()

        }

        btn_save_cont.setOnClickListener {
            addNewContory()

        }


        return view
    }

    private fun addNewContory() {
        if (input_contory.text.toString().isNotEmpty() && contory_price.text.toString()
                .isNotEmpty()
        ) {
            val newContort = input_contory.text.toString()
            val newPrice = contory_price.text.toString()
            var newCitys = ArrayList<String>()
            var firestore = FirebaseFirestore.getInstance()

            prog_add_contory.visibility = View.VISIBLE
            var dp1 = firestore.collection("city").document("data")
            dp1.get().addOnSuccessListener {
                var data = it.toObject(Citys::class.java)!!
                var newData = data

                newData.cit.put(newContort, newCitys)
                newData.contryprice.put(newContort, newPrice)

                dp1.set(newData).addOnSuccessListener {
                    prog_add_contory.visibility = View.GONE
                    Toast.makeText(btn_add_contory.context, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT)
                        .show()
                    dialog.cancel()
                }


            }
        }
    }

    private fun getConorys(){
        var firestore = FirebaseFirestore.getInstance().collection("city").document("data")
        firestore.get().addOnSuccessListener {
            var cantory = ArrayList<String>()
            var dataCity =  it.toObject(Citys::class.java)!!
            dataCity.cit.keys.forEach {cont->
            cantory.add(cont)

            }
            var adap = CantoryAdapter(cantory)
            contory_list.adapter = adap
        }
    }

    private fun imelmnt(view: View) {
        contory_list = view.findViewById(R.id.contory_list)
        btn_add_contory = view.findViewById(R.id.btn_add_contory)

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_contory)


		input_contory = dialog.findViewById(R.id.input_contory)
		contory_price = dialog.findViewById(R.id.contory_price)
		btn_save_cont = dialog.findViewById(R.id.btn_save_cont)
		prog_add_contory = dialog.findViewById(R.id.prog_add_contory)



        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        dialog.window?.setLayout((width * 1).toInt(), (height * 0.8).toInt())

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddContryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddContryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}