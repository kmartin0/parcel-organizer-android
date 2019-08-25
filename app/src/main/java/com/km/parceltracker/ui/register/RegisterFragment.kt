package com.km.parceltracker.ui.register

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseMVVMFragment
import com.km.parceltracker.databinding.FragmentRegisterBinding
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
        lavSuccess.addAnimatorUpdateListener { animation ->
            if (animation.animatedFraction == 1F) findNavController().navigateUp()
        }
        lavSuccess.playAnimation()
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun getToolbar(): MaterialToolbar? = defaultToolbar

}