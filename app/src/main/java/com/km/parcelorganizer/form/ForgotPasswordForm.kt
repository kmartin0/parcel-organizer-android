package com.km.parcelorganizer.form

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.km.parcelorganizer.R

class ForgotPasswordForm {

    val email = MutableLiveData<String>()

    val emailError = MutableLiveData<Int>()

    fun validateInput(): Boolean {
        return validateEmail()
    }

    fun validateEmail(): Boolean {
        var isValid: Boolean
        email.value.let {
            emailError.value = when {
                it.isNullOrBlank() -> {
                    isValid = false
                    R.string.error_required
                }
                !Patterns.EMAIL_ADDRESS.matcher(it).matches() -> {
                    isValid = false
                    R.string.error_invalid_email
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