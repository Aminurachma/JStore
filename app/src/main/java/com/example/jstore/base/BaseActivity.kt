package com.example.jstore.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.utils.AppProgressDialog

open class BaseActivity : AppCompatActivity() {

    lateinit var progress : AppProgressDialog

     fun setupProgress() {
        progress = AppProgressDialog(this)
        progress.setCancelable(false)
        progress.setCanceledOnTouchOutside(false)
    }

    fun hideProgressDialog() {
        progress.dismiss()
    }

    fun dismissProgressDialog(){
        progress?.dismiss()
    }
}