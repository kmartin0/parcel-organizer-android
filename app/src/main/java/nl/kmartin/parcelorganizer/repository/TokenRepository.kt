package nl.kmartin.parcelorganizer.repository

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import nl.kmartin.parcelorganizer.api.ParcelTrackerApi
import nl.kmartin.parcelorganizer.model.OAuth2Credentials
import nl.kmartin.parcelorganizer.model.User
import nl.kmartin.parcelorganizer.util.SharedPreferencesUtils
import io.reactivex.Single

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