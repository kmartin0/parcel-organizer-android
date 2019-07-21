package com.km.parceltracker.ui.createparcel

import android.app.Application
import android.webkit.URLUtil
import androidx.lifecycle.MutableLiveData
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.model.ParcelStatus
import com.km.parceltracker.repository.ParcelRepository
import com.km.parceltracker.util.SingleLiveEvent
import java.util.*

class CreateParcelViewModel(application: Application) : BaseViewModel(application) {
    private val parcelRepository = ParcelRepository(application.applicationContext)

    val title = MutableLiveData<String>()
    val sender = MutableLiveData<String>()
    val courier = MutableLiveData<String>()
    val trackingUrl = MutableLiveData<String>()
    val trackingStatus = MutableLiveData<ParcelStatusEnum>()

    val titleError = MutableLiveData<Int>()
    val senderError = MutableLiveData<Int>()
    val courierError = MutableLiveData<Int>()
    val trackingUrlError = MutableLiveData<Int>()
    val trackingStatusError = MutableLiveData<Int>()

    val parcelCreatedSuccess = SingleLiveEvent<Unit>()

    fun createParcel() {
        if (validateInput()) {
            val parcel =
                Parcel(
                    title.value!!,
                    sender.value,
                    courier.value,
                    trackingUrl.value,
                    ParcelStatus(1L, trackingStatus.value!!.name),
                    Date()
                )
            parcelRepository.insert(parcel)
            parcelCreatedSuccess.call()
        }
    }

    private fun validateInput(): Boolean {
        return validateTitle() and
                validateSender() and
                validateCourier() and
                validateTrackingUrl() and
                validateTrackingStatus()
    }

    private fun validateTitle(): Boolean {
        var isValid: Boolean
        title.value.let {
            titleError.value = when {
                it.isNullOrBlank() -> {
                    isValid = false
                    R.string.error_required
                }
                it.length > 45 -> {
                    isValid = false
                    R.string.error_max_characters_45
                }
                else -> {
                    isValid = true
                    null
                }
            }
        }
        return isValid
    }

    private fun validateSender(): Boolean {
        var isValid: Boolean
        sender.value.let {
            senderError.value = when {
                it.isNullOrEmpty() -> {
                    isValid = true
                    null
                }
                it.length > 45 -> {
                    isValid = false
                    R.string.error_max_characters_45
                }
                else -> {
                    isValid = true
                    null
                }
            }
        }
        return isValid
    }

    private fun validateCourier(): Boolean {
        var isValid: Boolean
        courier.value.let {
            courierError.value = when {
                it.isNullOrEmpty() -> {
                    isValid = true
                    null
                }
                it.length > 45 -> {
                    isValid = false
                    R.string.error_max_characters_45
                }
                else -> {
                    isValid = true
                    null
                }
            }
        }
        return isValid
    }

    private fun validateTrackingUrl(): Boolean {
        var isValid: Boolean
        trackingUrl.value.let {
            trackingUrlError.value = when {
                it.isNullOrEmpty() -> {
                    isValid = true
                    null
                }
                !URLUtil.isValidUrl(it) -> {
                    isValid = false
                    R.string.error_invalid_url
                }
                else -> {
                    isValid = true
                    null
                }
            }
        }
        return isValid
    }

    private fun validateTrackingStatus(): Boolean {
        var isValid: Boolean
        trackingStatus.value.let {
            trackingStatusError.value = when (it) {
                null -> {
                    isValid = true
                    R.string.error_required
                }
                else -> {
                    isValid = true
                    null
                }
            }
        }
        return isValid
    }

}