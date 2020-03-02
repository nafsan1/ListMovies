package com.example.newsmovie.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsmovie.R


fun getProgressDrawable(context: Context) : CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}
fun ImageView.loadImage(uri:String?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageurl")
fun loadImage(view:ImageView, url:String?){
    view.loadImage("https://image.tmdb.org/t/p/w154"+url, getProgressDrawable(view.context))
}

/*fun fadeOut(v: View?) {
  fadeOut(v?,null)
}

fun fadeOut(
    v: View,animListener:AnimListener
) {
    v.visibility = View.GONE
    v.alpha = 1.0f
    // Prepare the View for the animation
    v.animate()
        .setDuration(500)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animListener.onFinish()
                super.onAnimationEnd(animation)
            }
        })
        .alpha(0.0f)
}*/

interface AnimListener {
    fun onFinish()
}