package nl.kmartin.parcelorganizer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import nl.kmartin.parcelorganizer.repository.SettingsRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application)

    fun getDarkThemeObserver() = settingsRepository.isDarkThemeObserver
}