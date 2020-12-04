package com.km.parcelorganizer.ui.forgotpassword

import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import com.km.parcelorganizer.R
import com.km.parcelorganizer.base.BaseMVVMFragment
import com.km.parcelorganizer.databinding.FragmentForgotPasswordBinding
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.toolbar_default.*

class ForgotPasswordFragment :
    BaseMVVMFragment<FragmentForgotPasswordBinding, ForgotPasswordViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.passwordResetRequestSent.observe(this, {
            tvMessage.visibility = View.VISIBLE
        })
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<ForgotPasswordViewModel> = ForgotPasswordViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_forgot_password

    override fun getToolbar(): MaterialToolbar? = defaultToolbar

}