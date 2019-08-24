package com.km.parceltracker.ui.updateparcel

import android.app.Application
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.form.ParcelForm
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.repository.ParcelRepository
import com.km.parceltracker.util.SingleLiveEvent
import java.util.*

class UpdateParcelViewModel(application: Application) : BaseViewModel(application) {
    private val parcelRepository = ParcelRepository(application.applicationContext)

    private lateinit var parcelToUpdate: Parcel
    val parcelForm = ParcelForm()
    val parcelUpdateSuccess = SingleLiveEvent<Unit>()

    /**
     * Populate the [parcelForm] using [parcel]
     */
    fun populateParcelForm(parcel: Parcel) {
        parcelToUpdate = parcel
        parcelForm.apply {
            title.value = parcel.title
            sender.value = parcel.sender
            courier.value = parcel.courier
            trackingUrl.value = parcel.trackingUrl
            parcelStatusEnum.value = parcel.parcelStatus.status
        }
    }

    /**
     * If the input is valid then [parcelRepository] is used to update the [parcelToUpdate]
     */
    fun updateParcel() {
        if (parcelForm.validateInput()) {
            parcelForm.createParcelObject()?.let {
                it.id = parcelToUpdate.id
                it.lastUpdated = Date()
                parcelRepository.updateParcel(it)
                parcelUpdateSuccess.call()
            }
        }
    }

}