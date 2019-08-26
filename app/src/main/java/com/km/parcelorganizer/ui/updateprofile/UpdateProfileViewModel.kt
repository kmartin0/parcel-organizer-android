package com.km.parcelorganizer.ui.updateprofile

import android.app.Application
import com.km.parcelorganizer.R
import com.km.parcelorganizer.api.error.ApiError
import com.km.parcelorganizer.base.BaseViewModel
import com.km.parcelorganizer.form.UpdateProfileForm
import com.km.parcelorganizer.model.User
import com.km.parcelorganizer.repository.UserRepository
import com.km.parcelorganizer.util.SingleLiveEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UpdateProfileViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    private val userToUpdate = userRepository.getLoggedInUser()
    val profileUpdateSuccess = SingleLiveEvent<Any>()

    val updateProfileForm = UpdateProfileForm().apply {
        email.value = userToUpdate?.email
        name.value = userToUpdate?.name
    }

    fun updateProfile() {
        if (isLoading.value == false && updateProfileForm.validateInput(
                userToUpdate?.email,
                userToUpdate?.name
            )
        ) {
            userRepository.updateUser(
                userToUpdate!!.id,
                updateProfileForm.email.value!!,
                updateProfileForm.name.value!!,
                updateProfileForm.password.value!!
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<User> {
                    override fun onSuccess(t: User) {
                        stopLoading()
                        profileUpdateSuccess.call()
                    }

                    override fun onSubscribe(d: Disposable) {
                        startLoading()
                    }

                    override fun onError(e: Throwable) {
                        stopLoading()
                        handleApiError(e) { it?.let { apiError -> handleUpdateUserApiError(apiError) } }
                    }
                })
        }
    }

    private fun handleUpdateUserApiError(apiError: ApiError) {
        when (apiError.error) {
            ApiError.FORBIDDEN -> { // Check if filled in current password is correct.
                apiError.details?.forEach { targetError ->
                    if (targetError.target == "password") updateProfileForm.passwordError.value =
                        R.string.current_password_incorrect
                }
            }
            ApiError.ALREADY_EXISTS -> { // Check if email is unique.
                updateProfileForm.emailError.value = R.string.already_exists
            }
        }
    }
}