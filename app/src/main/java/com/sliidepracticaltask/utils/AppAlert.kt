package com.sliidepracticaltask.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sliidepracticaltask.R
import com.google.android.material.snackbar.Snackbar



fun Fragment.showError(view: View?, message: Int) {
    view ?: return
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    val snackBarView = snackbar.view
    snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.error_message))
    snackbar.show()
}

fun Fragment.showSuccess(view: View?, message: Int) {
    view ?: return
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    val snackBarView = snackbar.view
    snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.success_message))
    snackbar.show()
}

fun Fragment.showError(view: View?, message: String) {
    view ?: return
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    val snackBarView = snackbar.view
    snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.error_message))
    snackbar.show()
}

fun Context.showError(view: View?, message: String) {
    view ?: return
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    val snackBarView = snackbar.view
    snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.error_message))
    snackbar.show()
}

fun Fragment.showSuccess(view: View?, message: String) {
    view ?: return
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    val snackBarView = snackbar.view
    snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.success_message))
    snackbar.show()
}
