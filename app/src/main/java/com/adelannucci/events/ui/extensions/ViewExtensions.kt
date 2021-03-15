package com.adelannucci.events.ui.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackBar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG,
) {
    Snackbar.make(
        this,
        message,
        duration
    ).show()
}