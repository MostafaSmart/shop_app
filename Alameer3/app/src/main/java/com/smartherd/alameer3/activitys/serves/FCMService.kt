package com.smartherd.alameer3.activitys.serves

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers

import retrofit2.http.POST




interface FCMService {

    @POST("fcm/send")
    fun sendNotification(@Header("Content-Type") contentType: String,
                         @Header("Authorization") authorization: String,
                         @Body requestBody: RequestBody): Call<ResponseBody>

}