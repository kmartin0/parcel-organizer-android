package com.km.parcelorganizer.ui.register

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.km.parcelorganizer.R
import com.km.parcelorganizer.base.BaseMVVMFragment
import com.km.parcelorganizer.databinding.FragmentRegisterBinding
import com.km.parcelorganizer.util.playAnimation
import kotlinx.android.synthetic.main.form_register.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.toolbar_default.*


class RegisterFragment : BaseMVVMFragment<FragmentRegisterBinding, RegisterViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.registerSuccess.observe(this, Observer {
            onRegisterSuccess()
        })
        viewModel.alreadyExists.observe(this, Observer {
            tilEmail.error = getString(R.string.already_exists)
        })
    }

    private fun onRegisterSuccess() {
        btnRegister.isClickable = false
        lottieSuccess.playAnimation {
            findNavController().navigateUp()
        }
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun getToolbar(): MaterialToolbar? = defaultToolbar

}