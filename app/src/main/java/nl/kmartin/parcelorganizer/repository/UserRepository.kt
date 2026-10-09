package nl.kmartin.parcelorganizer.repository

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import nl.kmartin.parcelorganizer.api.ParcelTrackerApi
import nl.kmartin.parcelorganizer.api.request.ChangePasswordRequestBody
import nl.kmartin.parcelorganizer.api.request.ForgotPasswordRequestBody
import nl.kmartin.parcelorganizer.api.request.RegisterUserRequestBody
import nl.kmartin.parcelorganizer.api.request.ResetPasswordRequestBody
import nl.kmartin.parcelorganizer.api.request.UpdateUserRequestBody
import nl.kmartin.parcelorganizer.model.OAuth2Credentials
import nl.kmartin.parcelorganizer.model.User
import nl.kmartin.parcelorganizer.util.SharedPreferencesUtils
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
        SharedPreferencesUtils.getSharedPreferences(context).edit {
            putString(SharedPreferencesUtils.USER_KEY, Gson().toJson(user))
        }
    }

    /**
     * Clear user from Shared Preferences.
     */
    fun logoutUser() {
        SharedPreferencesUtils.getSharedPreferences(context).edit {
            remove(SharedPreferencesUtils.USER_KEY)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return getLoggedInUser()?.OAuth2Credentials != null
    }

    /**
     * @return Stored user, or null if no valid user session exists.
     */
    fun getLoggedInUser(): User? {
        val userString = SharedPreferencesUtils
            .getSharedPreferences(context)
            .getString(SharedPreferencesUtils.USER_KEY, null)
            ?: return null

        return runCatching {
            Gson().fromJson(userString, User::class.java)
        }.getOrNull()
    }
}