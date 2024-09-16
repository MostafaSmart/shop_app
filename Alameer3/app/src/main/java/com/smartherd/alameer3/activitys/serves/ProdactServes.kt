package com.smartherd.alameer3.activitys.serves

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.smartherd.alameer3.activitys.DataStrac.ProdactItemView
import com.smartherd.alameer3.activitys.models.Favorite
import com.smartherd.alameer3.activitys.models.Prodacts


class ProdactServes {
    var dp2=FirebaseFirestore.getInstance().collection("prodacts2")



    fun getDescountProdactsBySection(dolar:String,sectionid: String,callback: (ProdactItemView) -> Unit){
        val discount= HashMap<String, String>()
        if (dolar.isNotEmpty()){

            var docoments = ArrayList<DocumentSnapshot>()
            dp2.whereNotEqualTo("discount",discount).whereEqualTo("category",sectionid).get().addOnSuccessListener {
                val sortedDocuments = it.documents.sortedByDescending { dd->

                    dd.getDate("dateUpdate")

                }
                sortedDocuments.forEach { ss->

                    docoments.add(ss)
                }

                var prodactItemView = chickProdacts(docoments)

                callback(prodactItemView)

            }




        }

    }



    fun getDescountProdacts(dolar:String,limit:Boolean,count:Long,callback: (ProdactItemView) -> Unit){
        val discount= HashMap<String, String>()
        if (dolar.isNotEmpty()){

            var docoments = ArrayList<DocumentSnapshot>()

            if (limit){
                dp2.whereNotEqualTo("discount",discount).limit(count).get().addOnSuccessListener {
                    val sortedDocuments = it.documents.sortedByDescending { dd->

                        dd.getDate("dateUpdate")

                    }
                    sortedDocuments.forEach { ss->

                        docoments.add(ss)
                    }

                    var prodactItemView = chickProdacts(docoments)

                    callback(prodactItemView)

                }
            }
            else{
                dp2.whereNotEqualTo("discount",discount).get().addOnSuccessListener {
                    val sortedDocuments = it.documents.sortedByDescending { dd->

                        dd.getDate("dateUpdate")

                    }
                    sortedDocuments.forEach { ss->

                        docoments.add(ss)
                    }

                    var prodactItemView = chickProdacts(docoments)

                    callback(prodactItemView)

                }
            }






        }

    }


    fun getLastProdacts(dolar:String,limit:Boolean,count:Long,callback: (ProdactItemView) -> Unit){
        if (dolar.isNotEmpty()){
            var docoments = ArrayList<DocumentSnapshot>()


            if(limit){
                dp2.orderBy("dateAdded", Query.Direction.DESCENDING).limit(count).get().addOnSuccessListener {
                    it.documents.forEach {ss->
                        docoments.add(ss)
                    }
                    var prodactItemView = chickProdacts(docoments)

                    callback(prodactItemView)

                }

            }
            else{
                dp2.orderBy("dateAdded", Query.Direction.DESCENDING).get().addOnSuccessListener {
                    it.documents.forEach {ss->
                        docoments.add(ss)
                    }
                    var prodactItemView = chickProdacts(docoments)

                    callback(prodactItemView)

                }

            }


        }

    }

   fun getProdactBySection(dolar:String,sectionid:String,limit:Boolean,count:Long,callback: (ProdactItemView) -> Unit){
       if (dolar.isNotEmpty()){

           var docoments = ArrayList<DocumentSnapshot>()

           if (limit){
               dp2.whereEqualTo("category",sectionid).orderBy("dateAdded",Query.Direction.DESCENDING).limit(count).get().addOnSuccessListener {
                   it.documents.forEach { ss->
                       docoments.add(ss)
                   }

                   var prodactItemView = chickProdacts(docoments)

                   callback(prodactItemView)
               }
           }
           else{
               dp2.whereEqualTo("category",sectionid).orderBy("dateAdded",Query.Direction.DESCENDING).get().addOnSuccessListener {
                   it.documents.forEach { ss->
                       docoments.add(ss)
                   }

                   var prodactItemView = chickProdacts(docoments)

                   callback(prodactItemView)
               }
           }


       }


    }


    fun getHiddinProdacts(callback: (ProdactItemView) -> Unit){
        var prodacts = ArrayList<Prodacts>()
        var prodID = ArrayList<String>()
        dp2.whereEqualTo("state", "0").get().addOnSuccessListener {
            it.documents.forEach { prod ->
                var data = prod.toObject(Prodacts::class.java)!!
                prodacts.add(data)
                prodID.add(prod.id)

            }
            var prodactItemView = ProdactItemView(prodacts,prodID)
            callback(prodactItemView)
        }
    }

    fun getFavoriteProdact(dolar:String,userId:String,callback: (ProdactItemView?) -> Unit){
        if (dolar.isNotEmpty()){
            var favorite = FirebaseFirestore.getInstance().collection("favorite")
            favorite.document(userId).get().addOnSuccessListener {fadd->
                if(fadd.exists()){
                    var arrayFav = fadd.toObject(Favorite::class.java)!!
                    var co = 0

                    var docoments = ArrayList<DocumentSnapshot>()
                    arrayFav.favlist.forEach { prodId->

                        dp2.document(prodId).get().addOnSuccessListener {
                            if(it.exists()){
                                docoments.add(it)

                            }
                            co++

                            if(co == arrayFav.favlist.size){

                                var prodactItemView = chickProdacts(docoments)
                                callback(prodactItemView)

                            }
                        }

                    }



                }
                else{
                    callback(null)
                }
            }


        }


    }


    fun getProdactById(id:String,callback: (Prodacts?) -> Unit){
        dp2.document(id).get().addOnSuccessListener {
            if (it.exists()){
                val prodact = it.toObject(Prodacts::class.java)!!
                callback(prodact)
            }
            else{
                callback(null)
            }
        }

    }

    fun chickProdacts(sortedDocuments: ArrayList<DocumentSnapshot>):ProdactItemView{
        var userid = UserServes().getUserID()
        var allCorss = ArrayList<Prodacts>()
        var corsID = ArrayList<String>()


        sortedDocuments.forEach { posts->
            var data = posts.toObject(Prodacts::class.java)!!
            if(data.state == "0") {
                if (userid == "9cuzF1pt6DRAduNUd6ZGqWrGxeC2" || userid == "VfEvVuiJTdbCep7fQcYQVG6MivH3") {
                    allCorss.add(data)
                    corsID.add(posts.id)
                }
            }
            else if (data.state=="1"){
                allCorss.add(data)
                corsID.add(posts.id)
            }

        }
        var prodactItemView = ProdactItemView(allCorss, corsID)
        return prodactItemView
    }


}