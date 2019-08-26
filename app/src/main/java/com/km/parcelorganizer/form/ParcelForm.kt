package com.km.parcelorganizer.form

import android.webkit.URLUtil
import androidx.lifecycle.MutableLiveData
import com.km.parcelorganizer.R
import com.km.parcelorganizer.enums.ParcelStatusEnum
import com.km.parcelorganizer.model.Parcel

class ParcelForm {
    val title = MutableLiveData<String>()
    val sender = MutableLiveData<String>()
    val courier = MutableLiveData<String>()
    val trackingUrl = MutableLiveData<String>()
    val parcelStatusEnum = MutableLiveData<ParcelStatusEnum>()

    val titleError = MutableLiveData<Int>()
    val senderError = MutableLiveData<Int>()
    val courierError = MutableLiveData<Int>()
    val trackingUrlError = MutableLiveData<Int>()
    val parcelStatusEnumError = MutableLiveData<Int>()

    fun createParcelObject(): Parcel? {
        return null
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
     * Validate the [parcelStatus] value. Set [parcelStatusError] if not valid.
     * @return [Boolean] is [parcelStatus] not empty.
     */
    fun validateTrackingStatus(): Boolean {
        var isValid: Boolean
        parcelStatusEnum.value.let {
            parcelStatusEnumError.value = when (it) {
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