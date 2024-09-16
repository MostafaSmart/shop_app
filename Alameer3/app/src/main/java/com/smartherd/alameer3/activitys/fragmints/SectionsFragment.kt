package com.smartherd.alameer3.activitys.fragmints

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.adapders.SectionAdapder
import com.smartherd.alameer3.activitys.serves.GetSections

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SectionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SectionsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sections_list: androidx.recyclerview.widget.RecyclerView
    private lateinit var shim_sectio:ShimmerFrameLayout
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
        val view =  inflater.inflate(R.layout.fragment_sections, container, false)




        imelmnt(view)

        val layoutManager1 = LinearLayoutManager(activity)
        sections_list.layoutManager = layoutManager1

        GetSections().getAllSections{sectionItemView->

            shim_sectio.stopShimmer()
            // تطبيق الأنيميشن للاختفاء السلس
            shim_sectio.animate()
                .alpha(0f) // جعل الشفافية 0 (الاختفاء)
                .setDuration(500) // مدة الأنيميشن بالمللي ثانية
                .withEndAction {
                    // بعد انتهاء الأنيميشن، اجعل العنصر غير مرئي
                    shim_sectio.visibility = View.GONE
                    sections_list.visibility = View.VISIBLE // إظهار البيانات المطلوبة
                }
                .start()
            sections_list.adapter = SectionAdapder(sectionItemView)

        }









        return view
    }

    private fun imelmnt(view: View) {
        sections_list = view.findViewById(R.id.sections_list)
        shim_sectio = view.findViewById(R.id.shim_sectio)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SectionsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SectionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}