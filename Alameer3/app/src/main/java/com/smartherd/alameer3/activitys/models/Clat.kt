package com.smartherd.alameer3.activitys.models

import java.util.Date

data class Clat(var date: Date,
                var state:String,
                var products: ArrayList<ClatProdact>)
{
    constructor() : this(Date(),"", arrayListOf())
}