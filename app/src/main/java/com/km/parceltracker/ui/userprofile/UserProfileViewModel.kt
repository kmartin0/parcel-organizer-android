package com.km.parceltracker.ui.userprofile

import android.app.Application
import android.util.Log
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.repository.UserRepository

class UserProfileViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    var user = userRepository.getLoggedInUser()!!

    fun refreshUser() {
        user = userRepository.getLoggedInUser()!!
    }
}