package com.km.parceltracker.ui.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.model.User
import com.km.parceltracker.repository.UserRepository
import com.km.parceltracker.util.SingleLiveEvent

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val userRepository = UserRepository(application.applicationContext)
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginSuccess = SingleLiveEvent<Unit>()

    /**
     * Login the user using [userRepository] using [email] and [password]
     */
    fun login() {
        if (email.value.isNullOrBlank() || password.value.isNullOrBlank()) return
//        userRepository.persistUser(User(1L, email.value!!, email.value!!, password.value!!))
        loginSuccess.call()

        userRepository.authenticateUser(email.value!!, password.value!!)

    }

}