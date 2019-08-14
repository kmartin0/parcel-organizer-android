package com.km.parceltracker.util

import android.content.res.Resources
import android.widget.AutoCompleteTextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.km.parceltracker.enums.ParcelStatusEnum

@BindingAdapter("errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.error = errorMessage
}

@BindingAdapter("errorText")
fun setErrorMessage(view: TextInputLayout, @StringRes errorMessage: Int?) {
    try {
        view.error = if (errorMessage != null) view.context.getString(errorMessage) else null
    } catch (ex: Resources.NotFoundException) {
        view.error = null
    }
}

@BindingAdapter("setParcelStatusDropdownListener")
fun setParcelStatusDropdownListener(
    view: AutoCompleteTextView,
    trackingStatusLiveData: MutableLiveData<ParcelStatusEnum>?
) {
    view.setOnItemClickListener { parent, view, position, id ->
        trackingStatusLiveData?.value = ParcelStatusEnum.values()[position]
    }
}

@BindingAdapter("setParcelStatusDropdownText")
fun setParcelStatusDropdownText(
    view: AutoCompleteTextView,
    trackingStatusLiveData: MutableLiveData<ParcelStatusEnum>?
) {
    try {
        view.setText(trackingStatusLiveData?.value?.let {
            view.context.getString(it.stringResId)
        }, false)

    } catch (ex: Resources.NotFoundException) {
        view.setText(trackingStatusLiveData?.value?.name, false)
    }
}
