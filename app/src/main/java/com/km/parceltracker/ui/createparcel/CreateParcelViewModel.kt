package com.km.parceltracker.ui.createparcel

import android.app.Application
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.form.ParcelForm
import com.km.parceltracker.repository.ParcelRepository
import com.km.parceltracker.util.SingleLiveEvent

class CreateParcelViewModel(application: Application) : BaseViewModel(application) {
    private val parcelRepository = ParcelRepository(application.applicationContext)

    val parcelForm = ParcelForm()
    val parcelCreatedSuccess = SingleLiveEvent<Unit>()

    fun createParcel() {
        if (parcelForm.validateInput()) {
            parcelForm.getParcel()?.let {
                parcelRepository.insert(it)
                parcelCreatedSuccess.call()
            }
        }
    }

}