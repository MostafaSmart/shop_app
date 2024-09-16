package com.smartherd.alameer3.activitys.models

data class Users(var name:String,var email:String,var phone:String,var state:String,var Token:String,var admin:String) {
    constructor() : this("", "", "", "","","")
}
