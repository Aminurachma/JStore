package com.example.jstore.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constants {
    /*Collections*/
    const val USERS = "users"
    const val ADMIN = "admin"
    const val PRODUCTS = "products"

    const val IMAGE = "image"

    const val MYSHOP_PREFERENCE  = "MyJStorePrefs"
    const val LOGGED_IN_USERNAMEADMIN  ="logged_in_usernameAdmin"
    const val LOGGED_IN_USERNAME  ="logged_in_username"

    const val EXTRA_ADMIN_DETAILS = "extra_admin_details"
    const val EXTRA_USER_DETAILS = "extra_user_details"
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2


    const val PRODUCT_IMAGE = "product_image"

    const val USER_ID = "user_id"

    const val EXTRA_PRODUCT_ID = "extra_product_id"
    const val PRODUCT_ID = "product_id"

    const val EXTRA_PRODUCT_OWNER_ID = "extra_product_owner_id"
    const val DEFAULT_CARD_QUANTITY = "1"

    fun getFileExtension(activity: Activity, uri : Uri?) : String?{

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(activity.contentResolver.getType(uri!!))

    }

}