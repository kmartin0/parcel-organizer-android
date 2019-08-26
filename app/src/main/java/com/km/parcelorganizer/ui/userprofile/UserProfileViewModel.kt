package com.km.parcelorganizer.ui.userprofile

import android.app.Application
import com.km.parcelorganizer.base.BaseViewModel
import com.km.parcelorganizer.repository.UserRepository

class UserProfileViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    var user = userRepository.getLoggedInUser()!!

    fun refreshUser() {
        user = userRepository.getLoggedInUser()!!
    }
}