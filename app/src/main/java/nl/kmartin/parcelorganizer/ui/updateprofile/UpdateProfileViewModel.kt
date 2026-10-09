package nl.kmartin.parcelorganizer.ui.updateprofile

import android.app.Application
import nl.kmartin.parcelorganizer.R
import nl.kmartin.parcelorganizer.api.error.ApiError
import nl.kmartin.parcelorganizer.base.BaseViewModel
import nl.kmartin.parcelorganizer.enums.ApiErrorEnum
import nl.kmartin.parcelorganizer.form.UpdateProfileForm
import nl.kmartin.parcelorganizer.model.User
import nl.kmartin.parcelorganizer.repository.UserRepository
import nl.kmartin.parcelorganizer.util.SingleLiveEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UpdateProfileViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    private val userToUpdate = userRepository.getLoggedInUser()
    val profileUpdateSuccess = SingleLiveEvent<Unit>()

    val updateProfileForm = UpdateProfileForm().apply {
        email.value = userToUpdate?.email
        name.value = userToUpdate?.name
    }

    init {
        if (userToUpdate == null) {
            logout.value = Unit
        }
    }

    fun updateProfile() {
        val user = userToUpdate ?: return

        if (
            isLoading.value == false &&
            updateProfileForm.validateInput(user.email, user.name)
        ) {
            userRepository.updateUser(
                user.id,
                updateProfileForm.email.value!!,
                updateProfileForm.name.value!!,
                updateProfileForm.password.value!!
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<User> {
                    override fun onSuccess(t: User) {
                        stopLoading()
                        profileUpdateSuccess.value = Unit
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
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
            ApiErrorEnum.PERMISSION_DENIED -> { // Check if filled in current password is correct.
                apiError.details?.get("password")?.let {
                    updateProfileForm.passwordError.value = R.string.current_password_incorrect
                }
            }
            ApiErrorEnum.ALREADY_EXISTS -> { // Check if email is unique.
                updateProfileForm.emailError.value = R.string.already_exists
            }
            else -> {
            }
        }
    }
}