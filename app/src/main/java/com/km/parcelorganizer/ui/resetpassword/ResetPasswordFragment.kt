package com.km.parcelorganizer.ui.resetpassword

import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import com.km.parcelorganizer.R
import com.km.parcelorganizer.base.BaseMVVMFragment
import com.km.parcelorganizer.databinding.FragmentResetPasswordBinding
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.toolbar_default.*

class ResetPasswordFragment :
    BaseMVVMFragment<FragmentResetPasswordBinding, ResetPasswordViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToken()
        initObservers()
    }

    private fun setToken() {
        arguments?.let {
            viewModel.token = it.getString("token")
        }
    }

    private fun initObservers() {
        viewModel.error.observe(this, {
            tvMessage.setText(R.string.error_reset_password)
        })

        viewModel.success.observe(this, {
            tvMessage.setText(R.string.success_reset_password)
        })
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<ResetPasswordViewModel> = ResetPasswordViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_reset_password

    override fun getToolbar(): MaterialToolbar? = defaultToolbar
}