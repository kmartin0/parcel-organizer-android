package com.km.parceltracker.ui.login

import android.app.Application
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.form.LoginForm
import com.km.parceltracker.model.User
import com.km.parceltracker.repository.UserRepository
import com.km.parceltracker.util.SingleLiveEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val userRepository = UserRepository(application.applicationContext)
    val loginForm = LoginForm()
    val loginSuccess = SingleLiveEvent<Unit>()

    init {
        checkUserAlreadyLoggedIn()
    }

    private fun checkUserAlreadyLoggedIn() {
        if (userRepository.isUserLoggedIn()) loginSuccess.call()
    }

    /**
     * Login the user using [userRepository] using [email] and [password]
     */
    fun login() {
        if (loginForm.validateInput()) {
            userRepository.loginUser(loginForm.email.value!!, loginForm.password.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleObserver<User> {
                    override fun onSuccess(t: User) {
                        loginSuccess.call()
                        stopLoading()
                    }

                    override fun onSubscribe(d: Disposable) {
                        stopLoading()
                    }

                    override fun onError(e: Throwable) {
                        stopLoading()
                        val apiError = handleGlobalApiError(e)
                        when (apiError?.error) { // TODO: Check if I can add a function in handleGlobalApiError which will run the block inside the when of handleGlobalApiError
                            "invalid_grant" -> {
                                loginForm.emailError.value = R.string.error_login_credentials
                                loginForm.passwordError.value = R.string.error_login_credentials
                            }
                        }
                    }
                })
        }
    }

}