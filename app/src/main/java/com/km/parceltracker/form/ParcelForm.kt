package com.km.parceltracker.form

import android.webkit.URLUtil
import androidx.lifecycle.MutableLiveData
import com.km.parceltracker.R
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.model.ParcelStatus
import java.util.*

class ParcelForm {
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

    fun createParcelObject(): Parcel? {
        return if (validateInput()) {
            Parcel(
                title.value!!,
                sender.value,
                courier.value,
                trackingUrl.value,
                ParcelStatus(1L, trackingStatus.value!!),
                Date()
            )
        } else null
    }

    /**
     * @return [Boolean] is the parcel form input correct.
     */
    fun validateInput(): Boolean {
        return validateTitle() and
                validateSender() and
                validateCourier() and
                validateTrackingUrl() and
                validateTrackingStatus()
    }

    /**
     * Validate the [title] value. Set [titleError] if not valid.
     * @return [Boolean] is [title] not empty and has less than 46 characters
     */
    fun validateTitle(): Boolean {
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

    /**
     * Validate the [sender] value. Set [senderError] if not valid.
     * @return [Boolean] is [sender] has less than 46 characters
     */
    fun validateSender(): Boolean {
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

    /**
     * Validate the [courier] value. Set [courierError] if not valid.
     * @return [Boolean] is [courier] has less than 46 characters
     */
    fun validateCourier(): Boolean {
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

    /**
     * Validate the [trackingUrl] value. Set [trackingUrlError] if not valid.
     * @return [Boolean] is [trackingUrl] a valid url.
     */
    fun validateTrackingUrl(): Boolean {
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

    /**
     * Validate the [trackingStatus] value. Set [trackingStatusError] if not valid.
     * @return [Boolean] is [trackingStatus] not empty.
     */
    fun validateTrackingStatus(): Boolean {
        var isValid: Boolean
        trackingStatus.value.let {
            trackingStatusError.value = when (it) {
                null -> {
                    isValid = false
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