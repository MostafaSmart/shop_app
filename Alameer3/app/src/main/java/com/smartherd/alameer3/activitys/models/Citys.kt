package com.smartherd.alameer3.activitys.models

data class Citys(val cit: HashMap<String,ArrayList<String>> , val contryprice:HashMap<String,String>){
    constructor():this(hashMapOf(), hashMapOf())
}
