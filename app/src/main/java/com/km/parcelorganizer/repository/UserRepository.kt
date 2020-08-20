package com.km.parcelorganizer.repository

import android.content.Context
import com.google.gson.Gson
import com.km.parcelorganizer.api.ParcelTrackerApi
import com.km.parcelorganizer.api.request.*
import com.km.parcelorganizer.model.OAuth2Credentials
import com.km.parcelorganizer.model.User
import com.km.parcelorganizer.util.SharedPreferencesUtils
import io.reactivex.Completable
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

    fun registerUser(email: String, name: String, password: String): Single<User> {
        return parcelTrackerApi.registerUser(RegisterUserRequestBody(email, name, password))
    }

    fun updateUser(id: Long, email: String, name: String, currentPassword: String): Single<User> {
        return parcelTrackerApi.updateUser(UpdateUserRequestBody(id, email, name, currentPassword))
            .flatMap { loginUser(it.email, currentPassword) }
    }

    fun changePassword(currentPassword: String, newPassword: String): Completable {
        return parcelTrackerApi.changePassword(
            ChangePasswordRequestBody(
                currentPassword,
                newPassword
            )
        )
    }

    fun forgotPassword(email: String): Completable {
        return parcelTrackerApi.forgotPassword(ForgotPasswordRequestBody(email))
    }

    fun resetPassword(newPassword: String, token: String): Completable {
        return parcelTrackerApi.resetPassword(ResetPasswordRequestBody(newPassword, token))
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
        return SharedPreferencesUtils.getSharedPreferences(context)
            .contains(SharedPreferencesUtils.USER_KEY)
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