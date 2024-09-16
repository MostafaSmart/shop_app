package com.smartherd.alameer3.activitys.DataStrac

import com.smartherd.alameer3.activitys.models.Prodacts
import com.smartherd.alameer3.activitys.models.Section

data class SectionItemView(var sections:ArrayList<Section>, var sectID:ArrayList<String> ) {
    constructor():this(arrayListOf(), arrayListOf())

}