package com.smartherd.alameer3.activitys.models

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Orders(var date: Date, var info:HashMap<String,String>,
                  var products: ArrayList<ClatProdact>,var userId:String,var state:String)
{
    constructor() : this(Date(), hashMapOf(), arrayListOf(),"","")
}
