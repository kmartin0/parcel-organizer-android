package com.km.parcelorganizer.form

import androidx.lifecycle.MutableLiveData
import com.km.parcelorganizer.R

class LoginForm {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val emailError = MutableLiveData<Int>()
    val passwordError = MutableLiveData<Int>()

    fun validateInput(): Boolean {
        return validateEmail() and validatePassword()
    }

    fun validateEmail(): Boolean {
        var isValid: Boolean
        email.value.let {
            emailError.value = when {
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

    fun validatePassword(): Boolean {
        var isValid: Boolean
        password.value.let {
            passwordError.value = when {
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
}