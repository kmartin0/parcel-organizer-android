package com.km.parceltracker.repository

import android.content.Context
import com.google.gson.Gson
import com.km.parceltracker.enums.ParcelSearchingEnum
import com.km.parceltracker.enums.ParcelSortingEnum
import com.km.parceltracker.enums.SortOrderEnum
import com.km.parceltracker.model.ParcelsSortAndFilterConfig
import com.km.parceltracker.util.SharedPreferencesUtils

class SettingsRepository(val context: Context) {

    /**
     * Store [sortAndFilterConfig] in Shared Preferences.
     */
    fun setSortAndFilterSettings(sortAndFilterConfig: ParcelsSortAndFilterConfig) {
        SharedPreferencesUtils.getSharedPreferences(context).edit().run {
            putString(SharedPreferencesUtils.SORT_AND_FILTER_SETTINGS_KEY, Gson().toJson(sortAndFilterConfig))
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
        SharedPreferencesUtils.getSharedPreferences(context).run {
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

}