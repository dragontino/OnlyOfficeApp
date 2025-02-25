package com.onlyoffice.app.util

import android.util.Patterns

fun String.isEmailValid(): Boolean =
    this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()