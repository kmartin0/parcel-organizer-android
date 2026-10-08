package com.km.parcelorganizer.repository

import android.content.Context
import com.google.gson.Gson
import com.km.parcelorganizer.api.ParcelTrackerApi
import com.km.parcelorganizer.model.OAuth2Credentials
import com.km.parcelorganizer.model.User
import com.km.parcelorganizer.util.SharedPreferencesUtils
import io.reactivex.Single
import androidx.core.content.edit

class TokenRepository(val context: Context) {

    private val refreshTokenApi = ParcelTrackerApi.createRefreshTokenApi(context)

    fun refreshAccessToken(refreshToken: String): Single<OAuth2Credentials> {
        return refreshTokenApi.refreshToken(refreshToken)
    }

    fun getUserOAuth2Credentials(): OAuth2Credentials? {
        return getStoredUser()?.OAuth2Credentials
    }

    fun setUserOAuth2Credentials(oAuth2Credentials: OAuth2Credentials) {
        val user = getStoredUser() ?: return

        user.OAuth2Credentials = oAuth2Credentials

        SharedPreferencesUtils
            .getSharedPreferences(context)
            .edit {
                putString(SharedPreferencesUtils.USER_KEY, Gson().toJson(user))
            }
    }

    private fun getStoredUser(): User? {
        val userString = SharedPreferencesUtils
            .getSharedPreferences(context)
            .getString(SharedPreferencesUtils.USER_KEY, null)
            ?: return null

        return runCatching {
            Gson().fromJson(userString, User::class.java)
        }.getOrNull()
    }

}