package com.km.parcelorganizer.repository

import android.content.Context
import com.google.gson.Gson
import com.km.parcelorganizer.api.ParcelTrackerApi
import com.km.parcelorganizer.model.OAuth2Credentials
import com.km.parcelorganizer.model.User
import com.km.parcelorganizer.util.SharedPreferencesUtils
import io.reactivex.Single

class TokenRepository(val context: Context) {

    private val refreshTokenApi = ParcelTrackerApi.createRefreshTokenApi(context)

    fun refreshAccessToken(refreshToken: String): Single<OAuth2Credentials> {
        return refreshTokenApi.refreshToken(refreshToken)
    }

    fun getUserOAuth2Credentials(): OAuth2Credentials? {
        SharedPreferencesUtils.getSharedPreferences(context).run {
            val userString = getString(SharedPreferencesUtils.USER_KEY, null)
            return Gson().fromJson(userString, User::class.java).OAuth2Credentials
        }
    }

    fun setUserOAuth2Credentials(oAuth2Credentials: OAuth2Credentials) {
        val user = SharedPreferencesUtils.getSharedPreferences(context).run {
            val userString = getString(SharedPreferencesUtils.USER_KEY, null)
            Gson().fromJson(userString, User::class.java).apply { OAuth2Credentials = oAuth2Credentials }
        }

        SharedPreferencesUtils.getSharedPreferences(context).edit().run {
            putString(SharedPreferencesUtils.USER_KEY, Gson().toJson(user))
            apply()
        }
    }

}