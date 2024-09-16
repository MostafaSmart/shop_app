package com.smartherd.alameer3.activitys.Helpers

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ablanco.zoomy.Zoomy
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class ImageViewPigar {

     fun mainImgPager(images:ArrayList<String>,tabLayout:WormDotsIndicator ,viewPigerImage:ViewPager,contaxt:Context,activ:Activity?,pos:Int?,showDialog:Dialog?,imgViewShowALl:ImageView?) {

      var   handler = Handler()

        var adapter = object : PagerAdapter() {
            override fun getCount() = images.size

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val imageView = ImageView(contaxt)
                Picasso.get().load(images[position]).into(imageView)
                  imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//                if(activ!=null) {
//                    ZoomCuntrolPostPhoto(imageView, activ, contaxt)
//                }

                if(showDialog!=null){
                imageView.setOnClickListener {
                    Picasso.get().load(images[position]).into(imgViewShowALl)
                    imgViewShowALl!!.scaleType = ImageView.ScaleType.CENTER_CROP
                    showDialog.show()
//                    Picasso.get().load(images[position]).into(object  : com.squareup.picasso.Target {
//                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//
//                                if (bitmap!=null){
//                                    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 720, 960, false)
//                                    imgViewShowALl!!.setImageBitmap(resizedBitmap)
//                                    imgViewShowALl.scaleType = ImageView.ScaleType.CENTER_CROP
//                                    showDialog.show()
//                                }
//                            else{
//                                Toast.makeText(contaxt,"فشل",Toast.LENGTH_SHORT).show()
//                                }
//
//
//                            // عرض الصورة في ImageView
//
//                        }
//
//                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                            Toast.makeText(contaxt,e!!.message.toString(),Toast.LENGTH_SHORT).show()
//                        }
//
//                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                            // يتم استدعاء هذا الدالة أثناء تحميل الصورة
//                        }
//                    })
                }
                }


                imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                container.addView(imageView)
                return imageView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
         viewPigerImage.adapter = adapter

         tabLayout.attachTo(viewPigerImage)

         viewPigerImage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
             override fun onPageScrolled(
                 position: Int,
                 positionOffset: Float,
                 positionOffsetPixels: Int
             ) {

             }

             override fun onPageSelected(position: Int) {
                 val totalPages = adapter.count
                 val nextPage = position + 1


                 if (position <= nextPage){

                 }
                 else{
                     viewPigerImage.currentItem = 0
                 }

             }

             override fun onPageScrollStateChanged(state: Int) {

             }

         })




    }
  fun ZoomCuntrolPostPhoto(img: ImageView,activ: Activity,contaxt: Context){

        val bildar = Zoomy.Builder(activ).target(img)
            .animateZooming(true)
            .enableImmersiveMode(true)
            .tapListener {
                Toast.makeText(contaxt,"toch", Toast.LENGTH_SHORT).show()
            }
        bildar.register()


    }



}