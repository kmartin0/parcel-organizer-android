package com.km.parcelorganizer.ui.userprofile

import android.app.Application
import com.km.parcelorganizer.base.BaseViewModel
import com.km.parcelorganizer.model.User
import com.km.parcelorganizer.repository.SettingsRepository
import com.km.parcelorganizer.repository.UserRepository

class UserProfileViewModel(application: Application) : BaseViewModel(application) {

    private val userRepository = UserRepository(application.applicationContext)

    private val settingsRepository = SettingsRepository(application.applicationContext)

    var user: User? = null
        private set

    init {
        refreshUser()
    }

    fun refreshUser() {
        user = userRepository.getLoggedInUser()

        if (user == null) {
            logout.value = Unit
        }
    }

    fun isDarkTheme(): Boolean = settingsRepository.isDarkTheme

    fun onChangeDarkTheme(darkTheme: Boolean) {
        settingsRepository.isDarkTheme = darkTheme
    }
}