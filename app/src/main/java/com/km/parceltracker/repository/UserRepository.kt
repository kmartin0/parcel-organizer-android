package com.km.parceltracker.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.model.Authorization
import com.km.parceltracker.model.User
import com.km.parceltracker.util.Resource
import com.km.parceltracker.util.SharedPreferencesUtils
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserRepository(val context: Context) {

    private val parcelTrackerApi = ParcelTrackerApi.createApi(context)

    fun authenticateUser(email: String, password: String): MutableLiveData<Resource<Authorization>> {
        val state = MutableLiveData<Resource<Authorization>>()

        parcelTrackerApi.authenticateUser(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Authorization> {
                override fun onSuccess(t: Authorization) {
                    state.value = Resource.Success(t)
                }

                override fun onSubscribe(d: Disposable) {
                    state.value = Resource.Loading()
                }

                override fun onError(e: Throwable) {
                    state.value = Resource.Failure(e)
                }
            })

        return state

    }

    /**
     * Store [user] in Shared Preferences.
     */
    fun persistUser(user: User) {
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