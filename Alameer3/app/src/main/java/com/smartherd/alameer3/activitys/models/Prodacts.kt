package com.smartherd.alameer3.activitys.models

import java.util.Date

data class Prodacts(var productName: String,
                    var about:String,
                    val dateAdded: Date,
                    var dateUpdate:Date,
                    val category: String,
                    var sizes: ArrayList<HashMap<String, String>>,

                    var imageLinks: ArrayList<String>,
                    var colors: HashMap<String,ArrayList<String>>,
                    var contn: HashMap<String,String>,
                    var discount: HashMap<String, String>,
                    val state:String,
                    var downprice:HashMap<String,String>,
                    var expirDesc:Date?,
                    var imgSize:HashMap<String,String>,
                    var imgColor:HashMap<String,String>,



                    ){

    constructor():this("","",Date(),Date(),""
        , arrayListOf(),arrayListOf(), hashMapOf(), hashMapOf(),
        hashMapOf(),"1", hashMapOf(),Date(), hashMapOf(), hashMapOf()
    )




}
