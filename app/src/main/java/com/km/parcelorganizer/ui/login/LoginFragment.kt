package com.km.parcelorganizer.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.km.parcelorganizer.R
import com.km.parcelorganizer.base.BaseMVVMFragment
import com.km.parcelorganizer.databinding.FragmentLoginBinding

class LoginFragment : BaseMVVMFragment<FragmentLoginBinding, LoginViewModel>() {

    private val args: LoginFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun initObservers() {
        // When login is successful then navigate to the ParcelsFragment.
        viewModel.loginSuccess.observe(this, {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToParcelsFragment(
                    args.trackingUrl
                )
            )
        })
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_login

}