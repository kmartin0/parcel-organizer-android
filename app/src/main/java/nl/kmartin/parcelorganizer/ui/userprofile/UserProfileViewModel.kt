package nl.kmartin.parcelorganizer.ui.userprofile

import android.app.Application
import nl.kmartin.parcelorganizer.base.BaseViewModel
import nl.kmartin.parcelorganizer.model.User
import nl.kmartin.parcelorganizer.repository.SettingsRepository
import nl.kmartin.parcelorganizer.repository.UserRepository

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