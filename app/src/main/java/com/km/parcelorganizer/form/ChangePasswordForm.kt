package com.km.parcelorganizer.form

import androidx.lifecycle.MutableLiveData
import com.km.parcelorganizer.R

class ChangePasswordForm {
    val currentPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmNewPassword = MutableLiveData<String>()

    val currentPasswordError = MutableLiveData<Int>()
    val newPasswordError = MutableLiveData<Int>()
    val confirmNewPasswordError = MutableLiveData<Int>()

    fun validateInput(): Boolean {
        return validateCurrentPassword() and validateNewPassword() and validateConfirmNewPassword()
    }

    fun validateCurrentPassword(): Boolean {
        var isValid: Boolean
        currentPassword.value.let {
            currentPasswordError.value = when {
                it.isNullOrBlank() -> {
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

    fun validateNewPassword(): Boolean {
        var isValid: Boolean
        newPassword.value.let {
            newPasswordError.value = when {
                it.isNullOrBlank() -> {
                    isValid = false
                    R.string.error_required
                }
                it.length > 45 -> {
                    isValid = false
                    R.string.error_max_characters_45
                }
                it.length < 5 -> {
                    isValid = false
                    R.string.error_min_characters_6
                }
                else -> {
                    isValid = true
                    null
                }
            }
        }
        return isValid
    }

    fun validateConfirmNewPassword(): Boolean {
        var isValid: Boolean
        confirmNewPassword.value.let {
            confirmNewPasswordError.value = when {
                it.isNullOrBlank() -> {
                    isValid = false
                    R.string.error_required
                }
                it != newPassword.value -> {
                    isValid = false
                    R.string.error_confirm_password
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