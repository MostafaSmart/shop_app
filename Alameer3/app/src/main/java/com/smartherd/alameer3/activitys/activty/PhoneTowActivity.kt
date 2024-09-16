package com.smartherd.alameer3.activitys.activty

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.MainActivity
import com.smartherd.alameer3.activitys.serves.FCMService
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.math.log

class PhoneTowActivity : AppCompatActivity() {
    private lateinit var heder_no: EditText
    private lateinit var body_nov: EditText
    private lateinit var heder_bake: ImageView
    private lateinit var hedar_tital: TextView
    private lateinit var btn_finsh_crop: Button
    private lateinit var crop_heder: LinearLayout
    private lateinit var chick_with_image: CheckBox
    private lateinit var prog_send_notf: ProgressBar
    private lateinit var card_whith_image: LinearLayout
    private lateinit var btn_add_image_notfigtion: Button
    private lateinit var compressedImageUri: Uri
    private lateinit var show_notf_image:ImageView


    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->


        if(uri !=null) {

            GlobalScope.launch(Dispatchers.Main) {
                val compressedImageFile = compress(this@PhoneTowActivity,uri)
                compressedImageUri = Uri.fromFile(compressedImageFile)


                show_notf_image.visibility = View.VISIBLE
                show_notf_image.setImageURI(compressedImageUri)


            }


        }




    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_tow)





        imelmnt()


        btn_add_image_notfigtion.setOnClickListener {
            getContent.launch("image/*")

        }



        chick_with_image.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                card_whith_image.visibility = View.VISIBLE
            }
            else{
                card_whith_image.visibility = View.GONE
            }
        }


        btn_finsh_crop.setOnClickListener {

            if(heder_no.text.toString().isNotEmpty() && body_nov.text.toString().isNotEmpty()){
                var tital = heder_no.text.toString()
                var body = body_nov.text.toString()

                var jsonObject = JSONObject()
                var notificationObject = JSONObject()

                jsonObject.put("condition", "'sendAll' in topics")
                jsonObject.put("priority", "high")

                notificationObject.put("title", tital)
                notificationObject.put("body", body)



                if (chick_with_image.isChecked == true) {
                    if (compressedImageUri !=null){
                        prog_send_notf.visibility = View.VISIBLE
                        val newpostRef = FirebaseFirestore.getInstance().collection("notfgion").document()
                        var firebaseStorage = FirebaseStorage.getInstance()
                        val ref = firebaseStorage.getReference().child("notfgion").child(newpostRef.id).child(UUID.randomUUID().toString())

                        ref.putFile(compressedImageUri).addOnCompleteListener { task1 ->

                            if (task1.isSuccessful) {
                                var imgLink = ref.downloadUrl

                                notificationObject.put("image",imgLink)
                                jsonObject.put("notification", notificationObject)

                                sendNotifction(jsonObject)

                            }

                        }
                    }

                }
                else{
                    jsonObject.put("notification", notificationObject)
                    sendNotifction(jsonObject)
                }






            }




        }

    }


    fun sendNotifction(jsonObject:JSONObject){
        val retrofit = Retrofit.Builder().baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val fcmApiService = retrofit.create(FCMService::class.java)

        var jsonStrin = jsonObject.toString()
        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonStrin)
        val call = fcmApiService.sendNotification(
            "application/json",
            "key=AAAAT05EJ0A:APA91bFl5oudhSbFyQxCNRpV6cx9jOciow-0HmdrRNyPGdtomKN3vRqbsXM9t0928Do4IwCLoqjsDUWVdoCNV2CIoNW_w3tHJqgkBY1Yf9TWUQgVzuwEvyTOYDZQgTHUt9OfvnMJOHVM",
            requestBody
        )




        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // يتم استدعاء هذه الوظيفة في حالة استلام استجابة صحيحة
                if (response.isSuccessful) {
                    Toast.makeText(this@PhoneTowActivity,"يتم ارسال الاشعارات",Toast.LENGTH_LONG).show()
                    prog_send_notf.visibility = View.GONE

                } else {
                    Toast.makeText(this@PhoneTowActivity,response.body().toString(),Toast.LENGTH_LONG).show()


                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // يتم استدعاء هذه الوظيفة في حالة حدوث خطأ أثناء الاتصال
                // التعامل مع الخطأ هنا
                Toast.makeText(this@PhoneTowActivity,t.message.toString(),Toast.LENGTH_LONG).show()

            }
        })

    }

    suspend fun compress(context: Context, uri: Uri): File {
        return withContext(Dispatchers.IO) {
            Compressor.compress(context, FileUtil.from(context,uri)) {
                default()
            }
        }
    }

    object FileUtil {
        fun from(context: Context, uri: Uri): File {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("image", null, context.cacheDir)
            tempFile.deleteOnExit()
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return tempFile
        }
    }


    private fun imelmnt() {

        heder_no = findViewById(R.id.heder_no)
        body_nov = findViewById(R.id.body_nov)
        crop_heder = findViewById(R.id.crop_heder)
        heder_bake = findViewById(R.id.heder_bake)
        hedar_tital = findViewById(R.id.hedar_tital)
        prog_send_notf = findViewById(R.id.prog_send_notf)
        btn_finsh_crop = findViewById(R.id.btn_finsh_crop)
        chick_with_image = findViewById(R.id.chick_with_image)
        card_whith_image = findViewById(R.id.card_whith_image)
        btn_add_image_notfigtion = findViewById(R.id.btn_add_image_notfigtion)
        show_notf_image = findViewById(R.id.show_notf_image)


    }
}