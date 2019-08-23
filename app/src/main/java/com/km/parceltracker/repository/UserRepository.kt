package com.km.parceltracker.repository

import android.content.Context
import com.google.gson.Gson
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.model.OAuth2Credentials
import com.km.parceltracker.model.User
import com.km.parceltracker.util.SharedPreferencesUtils
import io.reactivex.Single


class UserRepository(val context: Context) {

    private val parcelTrackerApi = ParcelTrackerApi.createApi(context)

    fun loginUser(email: String, password: String): Single<User> {
        return authenticateUser(email, password)
            .flatMap { oAuth2Credentials ->
                getUser("Bearer ${oAuth2Credentials.accessToken}")
                    .map { user ->
                        user.also {
                            it.OAuth2Credentials = oAuth2Credentials
                            persistUser(it)
                        }
                    }
            }
    }

    private fun authenticateUser(email: String, password: String): Single<OAuth2Credentials> {
        return parcelTrackerApi.authenticateUser(email, password)
    }

    private fun getUser(token: String): Single<User> {
        return parcelTrackerApi.getUser(token)
    }

    /**
     * Store [user] in Shared Preferences.
     */
    private fun persistUser(user: User) {
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
}