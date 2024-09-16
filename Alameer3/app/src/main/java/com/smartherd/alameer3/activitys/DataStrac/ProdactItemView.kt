package com.smartherd.alameer3.activitys.DataStrac

import com.smartherd.alameer3.activitys.models.Prodacts

data class ProdactItemView(var prodacts:ArrayList<Prodacts>,var prodaID:ArrayList<String>){
    constructor():this(arrayListOf(), arrayListOf())
}
