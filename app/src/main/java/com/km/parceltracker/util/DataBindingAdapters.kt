package com.km.parceltracker.util

import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("bind:errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.error = errorMessage
}

@BindingAdapter("bind:errorText")
fun setErrorMessage(view: TextInputLayout, @StringRes errorMessage: Int?) {
    view.error = if (errorMessage != null) view.context.getString(errorMessage) else null
}
