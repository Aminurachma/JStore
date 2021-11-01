package com.example.jstore.utils

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.jstore.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber
import java.text.DecimalFormat

fun <T> AppCompatActivity.pushActivity(targetClass: Class<T>) {
    startActivity(Intent(this, targetClass))
}

fun <T> Fragment.pushActivity(targetClass: Class<T>) {
    startActivity(Intent(requireContext(), targetClass))
}

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

fun View.toInvisible() {
    visibility = View.INVISIBLE
}

fun View.visible(status: Boolean) {
    if (status) this.toVisible() else this.toGone()
}

fun View.rotateAnimation(startRadius: Float, endRadius: Float) {
    ObjectAnimator.ofFloat(this, View.ROTATION, startRadius, endRadius).setDuration(300).start()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) = ContextCompat.getDrawable(this, drawable)

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun Context.getColorStateListCompat(@ColorRes color: Int) = ContextCompat.getColorStateList(this, color)

fun getFragmentWidthPercentage(percentage: Int): Int {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    return (rect.width() * percent).toInt()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun TextInputEditText.clearText() {
    setText("")
    clearFocus()
    context.hideKeyboard(this)
}

fun AppCompatEditText.addMoneyWatcher() {
    addTextChangedListener(MoneyTextWatcher(this))
}

fun logDebug(message: String, throwable: Throwable? = null) {
    Timber.d(throwable, message)
}

fun logError(message: String, throwable: Throwable? = null) {
    Timber.e(throwable, message)
}

fun Int.formatPrice(): String {
    val formatter = DecimalFormat("#,###")
    return "Rp. ${formatter.format(this)}"
}

fun Long.formatPrice(): String {
    val formatter = DecimalFormat("#,###")
    return "Rp. ${formatter.format(this)}"
}

fun Activity.imagePicker(launcher: ActivityResultLauncher<Intent>) {
    ImagePicker.with(this)
        .crop()
        .createIntent { intent ->
            launcher.launch(intent)
        }
}

fun Fragment.imagePicker(launcher: ActivityResultLauncher<Intent>) {
    ImagePicker.with(this)
        .crop()
        .createIntent { intent ->
            launcher.launch(intent)
        }
}