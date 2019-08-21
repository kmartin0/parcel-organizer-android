package com.km.parceltracker.repository

import android.content.Context
import com.google.gson.Gson
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.model.Authorization
import com.km.parceltracker.model.User
import com.km.parceltracker.util.SharedPreferencesUtils
import io.reactivex.Single

class UserRepository(val context: Context) {

    private val refreshTokenApi = ParcelTrackerApi.createRefreshTokenApi()

    /**
     * Store [user] in Shared Preferences.
     */
    fun loginUser(user: User) {
        SharedPreferencesUtils.getSharedPreferences(context).edit().run {
            putString(SharedPreferencesUtils.USER_KEY, Gson().toJson(user))
            apply()
        }
    }

    /**
     * Clear user from Shared Preferences.
     */
    fun logoutUser() {
        SharedPreferencesUtils.getSharedPreferences(context).edit().run {
            remove(SharedPreferencesUtils.USER_KEY)
            apply()
        }
    }

    /**
     * @return Boolean if Shared Preferences contains a [User]
     */
    fun isUserLoggedIn(): Boolean {
        return SharedPreferencesUtils.getSharedPreferences(context).contains(SharedPreferencesUtils.USER_KEY)
    }

    /**
     * @return User? user object from Shared Preferences.
     */
    fun getLoggedInUser(): User? {
        SharedPreferencesUtils.getSharedPreferences(context).run {
            val userString = getString(SharedPreferencesUtils.USER_KEY, null)
            return Gson().fromJson(userString, User::class.java)
        }
    }

    fun refreshAccessToken(refreshToken : String): Single<Authorization> {
        return refreshTokenApi.refreshToken(refreshToken = refreshToken)
    }

    fun setUserAuthentication(authorization: Authorization) {
        getLoggedInUser()?.authorization = authorization
    }

}