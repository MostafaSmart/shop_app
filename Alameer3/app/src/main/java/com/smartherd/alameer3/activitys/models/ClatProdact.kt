package com.smartherd.alameer3.activitys.models

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class ClatProdact(var prodid: String,
                       var color:String,
                       var much: String,
                       var sizeprice: HashMap<String, String>
                  ) {

    constructor() : this("","","", hashMapOf())
}