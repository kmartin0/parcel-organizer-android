package com.km.parceltracker.repository

import android.content.Context
import com.google.gson.Gson
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.model.Authorization
import com.km.parceltracker.model.User
import com.km.parceltracker.util.SharedPreferencesUtils
import io.reactivex.Single

class TokenRepository(val context: Context) {

    private val refreshTokenApi = ParcelTrackerApi.createRefreshTokenApi()

    fun refreshAccessToken(refreshToken: String): Single<Authorization> {
        return refreshTokenApi.refreshToken(refreshToken = refreshToken)
    }

    fun getUserAuthentication() : Authorization? {
        SharedPreferencesUtils.getSharedPreferences(context).run {
            val userString = getString(SharedPreferencesUtils.USER_KEY, null)
            return Gson().fromJson(userString, User::class.java).authorization
        }
    }

    fun setUserAuthentication(authentication: Authorization) {
      val user = SharedPreferencesUtils.getSharedPreferences(context).run {
            val userString = getString(SharedPreferencesUtils.USER_KEY, null)
            Gson().fromJson(userString, User::class.java).apply { authorization = authentication }
        }

        SharedPreferencesUtils.getSharedPreferences(context).edit().run {
            putString(SharedPreferencesUtils.USER_KEY, Gson().toJson(user))
            apply()
        }
    }

}