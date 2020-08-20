package com.km.parcelorganizer.form

import androidx.lifecycle.MutableLiveData
import com.km.parcelorganizer.R

class ResetPasswordForm {
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    val passwordError = MutableLiveData<Int>()
    val confirmPasswordError = MutableLiveData<Int>()

    fun validateInput(): Boolean {
        return validatePassword() and
                validateConfirmPassword()
    }

    fun resetForm() {
        password.value = null
        confirmPassword.value = null

        passwordError.value = null
        confirmPasswordError.value = null
    }

    fun validatePassword(): Boolean {
        var isValid: Boolean
        password.value.let {
            passwordError.value = when {
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

    fun validateConfirmPassword(): Boolean {
        var isValid: Boolean
        confirmPassword.value.let {
            confirmPasswordError.value = when {
                it.isNullOrBlank() -> {
                    isValid = false
                    R.string.error_required
                }
                it != password.value -> {
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