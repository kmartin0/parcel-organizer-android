package com.km.parcelorganizer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.km.parcelorganizer.repository.SettingsRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application)

    fun getDarkThemeObserver() = settingsRepository.isDarkThemeObserver
}