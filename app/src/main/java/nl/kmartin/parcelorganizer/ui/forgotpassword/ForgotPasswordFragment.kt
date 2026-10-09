package nl.kmartin.parcelorganizer.ui.forgotpassword

import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import nl.kmartin.parcelorganizer.R
import nl.kmartin.parcelorganizer.base.BaseMVVMFragment
import nl.kmartin.parcelorganizer.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment :
    BaseMVVMFragment<FragmentForgotPasswordBinding, ForgotPasswordViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.passwordResetRequestSent.observe(viewLifecycleOwner, {
            binding.tvMessage.visibility = View.VISIBLE
        })
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<ForgotPasswordViewModel> = ForgotPasswordViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_forgot_password

    override fun getToolbar(): MaterialToolbar = binding.toolbarLayout.defaultToolbar

}