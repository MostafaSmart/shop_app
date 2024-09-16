package com.smartherd.alameer3.activitys.serves

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.alameer3.activitys.models.Section
import com.smartherd.alameer3.activitys.models.UserDate
import com.smartherd.alameer3.activitys.models.Users

class UserServes {

    fun getUserData(userId:String,callback: (UserDate?) -> Unit){

        var dp = FirebaseFirestore.getInstance().collection("users").document(userId)

        dp.get().addOnSuccessListener {us->
            if (us.exists()){
                var user1 =  us.toObject(Users::class.java)

                getUserDolar(user1!!.state){dolar->
                    var userdate = UserDate(user1, dolar)
                    callback(userdate)

                }
            }
            else{
                callback(null)
            }


        }


    }

    fun getUserDolar(state:String,callback: (String) -> Unit){

        var dolareDB = FirebaseFirestore.getInstance().collection("admin").document("dolarV")

        var dolar =""
        dolareDB.get().addOnSuccessListener {
            if (state == "شمال"){
                dolar = it.get("dolarup").toString()
            }
            else if (state =="جنوب"){
                dolar = it.get("dolardown").toString()
            }
            else{
                dolar = it.get("dolarup").toString()
            }

            callback(dolar)
        }


    }

    fun getUserID():String{

        var userId =""
        if( FirebaseAuth.getInstance().currentUser!=null){
            userId =  FirebaseAuth.getInstance().currentUser!!.uid
        }
        return  userId



    }



}