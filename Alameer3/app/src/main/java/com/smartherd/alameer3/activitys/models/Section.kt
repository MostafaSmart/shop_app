package com.smartherd.alameer3.activitys.models

import java.util.Date

data class Section(var name:String,var date:Date,var order:String){
    constructor():this("",Date(),"")
}
