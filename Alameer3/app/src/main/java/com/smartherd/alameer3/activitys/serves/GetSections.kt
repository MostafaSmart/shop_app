package com.smartherd.alameer3.activitys.serves

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.activitys.DataStrac.SectionItemView
import com.smartherd.alameer3.activitys.adapders.SectionAdapder
import com.smartherd.alameer3.activitys.models.Prodacts
import com.smartherd.alameer3.activitys.models.Section

class GetSections {

    var firestore = FirebaseFirestore.getInstance()
    fun getAllSections(callback: (SectionItemView) -> Unit){

        var dp = firestore.collection("sections").orderBy("order")

        var sections = ArrayList<Section>()
        var sectionsId = ArrayList<String>()

        dp.get().addOnSuccessListener {
            it.documents.forEach {sec->

                sections.add(sec.toObject(Section::class.java)!!)
                sectionsId.add(sec.id)

            }

            var sectionItemView = SectionItemView(sections,sectionsId)
            callback(sectionItemView)

        }



    }

    fun getSectionByID(sectId:String,callback: (Section?) -> Unit){

        firestore.collection("sections").document(sectId).get().addOnSuccessListener {
            if(it.exists()){
                var secti = it.toObject(Section::class.java)!!
                callback(secti)
            }
            else{
                callback(null)
            }
        }

    }

}