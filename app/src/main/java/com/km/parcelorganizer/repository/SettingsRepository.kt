package com.km.parcelorganizer.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.km.parcelorganizer.enums.ParcelSearchingEnum
import com.km.parcelorganizer.enums.ParcelSortingEnum
import com.km.parcelorganizer.enums.SortOrderEnum
import com.km.parcelorganizer.model.ParcelsSortAndFilterConfig
import com.km.parcelorganizer.util.SharedPreferencesUtils

class SettingsRepository(val context: Context) {


    private var preferences: SharedPreferences =
        SharedPreferencesUtils.getSharedPreferences(context)

    /**
     * Store [sortAndFilterConfig] in Shared Preferences.
     */
    fun setSortAndFilterSettings(sortAndFilterConfig: ParcelsSortAndFilterConfig) {
        preferences.edit().run {
            putString(
                SharedPreferencesUtils.SORT_AND_FILTER_SETTINGS_KEY,
                Gson().toJson(sortAndFilterConfig)
            )
            apply()
        }
    }

    /**
     * Retrieve [ParcelsSortAndFilterConfig] from Shared Preferences. If no config is present store and return the default.
     * Sort and filter preferences.
     *
     * @return [ParcelsSortAndFilterConfig] retrieved from Shared Preferences.
     */
    fun getSortAndFilterSettings(): ParcelsSortAndFilterConfig {
        preferences.run {
            val gSon = getString(SharedPreferencesUtils.SORT_AND_FILTER_SETTINGS_KEY, null)
            return if (gSon == null) {
                setSortAndFilterSettings(
                    ParcelsSortAndFilterConfig(
                        sortBy = ParcelSortingEnum.TITLE,
                        sortOrder = SortOrderEnum.ASCENDING,
                        searchQuery = null,
                        searchBy = ParcelSearchingEnum.TITLE,
                        ordered = true,
                        sent = true,
                        delivered = true
                    )
                )
                getSortAndFilterSettings()
            } else Gson().fromJson(gSon, ParcelsSortAndFilterConfig::class.java)
        }
    }

    /**
     * Get or set the current dark theme from/in shared preferences.
     */
    var isDarkTheme: Boolean = false
        get() = preferences.getBoolean(SharedPreferencesUtils.DARK_THEME, false)
        set(value) {
            preferences.edit().putBoolean(SharedPreferencesUtils.DARK_THEME, value).apply()
            field = value
        }

    /**
     * Observer that will notify observers for dark theme setting changes from shared preferences.
     */
    private val _isDarkThemeObserver: MutableLiveData<Boolean> = MutableLiveData()
    val isDarkThemeObserver: LiveData<Boolean>
        get() = _isDarkThemeObserver

    /**
     * Listener for shared preferences that will update [_isDarkThemeObserver] if the dark theme
     * setting has changed.
     */
    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                SharedPreferencesUtils.DARK_THEME -> {
                    _isDarkThemeObserver.value = isDarkTheme
                }
            }
        }

    init {
        _isDarkThemeObserver.value = isDarkTheme
        preferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

}