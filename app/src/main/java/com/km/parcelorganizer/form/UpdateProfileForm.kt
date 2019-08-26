package com.km.parcelorganizer.form

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.km.parcelorganizer.R

class UpdateProfileForm {
    val email = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val emailError = MutableLiveData<Int>()
    val nameError = MutableLiveData<Int>()
    val passwordError = MutableLiveData<Int>()

    fun validateInput(prevEmail: String?, prevName: String?): Boolean {
        return validateEmail() and
                validateName() and
                validatePassword() and
                validateChangesMade(prevEmail, prevName)
    }

    fun validateEmail(): Boolean {
        var isValid: Boolean
        email.value.let {
            emailError.value = when {
                it.isNullOrBlank() -> {
                    isValid = false
                    R.string.error_required
                }
                it.length > 45 -> {
                    isValid = false
                    R.string.error_max_characters_45
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

    fun validateName(): Boolean {
        var isValid: Boolean
        name.value.let {
            nameError.value = when {
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
                it.length < 3 -> {
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

    fun validateChangesMade(prevEmail: String?, prevName: String?): Boolean {
        return if (prevEmail == email.value && prevName == name.value) {
            emailError.value = R.string.change_at_least_1
            nameError.value = R.string.change_at_least_1
            false
        } else true
    }
}