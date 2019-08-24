package com.km.parceltracker.ui.register

import android.app.Application
import com.km.parceltracker.api.ApiError
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.form.RegisterForm
import com.km.parceltracker.model.User
import com.km.parceltracker.repository.UserRepository
import com.km.parceltracker.util.SingleLiveEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RegisterViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    val registerForm = RegisterForm()
    val registerSuccess = SingleLiveEvent<Unit>()
    val alreadyExists = SingleLiveEvent<Unit>()

    fun register() {
        if (registerForm.validateInput()) {
            userRepository.registerUser(
                registerForm.email.value!!,
                registerForm.name.value!!,
                registerForm.password.value!!
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleObserver<User> {
                    override fun onSuccess(t: User) {
                        stopLoading()
                        registerSuccess.call()
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                        startLoading()
                    }

                    override fun onError(e: Throwable) {
                        stopLoading()
                        handleApiError(e) {
                            when (it?.error) {
                                ApiError.ALREADY_EXISTS -> alreadyExists.call()
                            }
                        }
                    }

                })
        }
    }
}