package com.km.parcelorganizer.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtils {
    companion object {
        private const val SHARED_PREFERENCES_FILE = "PARCEL_TRACKER_SHARED_PREFS"
        const val USER_KEY = "USER_KEY"
        const val SORT_AND_FILTER_SETTINGS_KEY = "SORT_AND_FILTER_SETTINGS_KEY"

        fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
        }
    }
}