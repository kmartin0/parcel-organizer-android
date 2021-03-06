package com.km.parcelorganizer.ui.changepassword

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.km.parcelorganizer.R
import com.km.parcelorganizer.base.BaseMVVMFragment
import com.km.parcelorganizer.databinding.FragmentChangePasswordBinding
import com.km.parcelorganizer.util.playAnimation

class ChangePasswordFragment :
    BaseMVVMFragment<FragmentChangePasswordBinding, ChangePasswordViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {

        viewModel.changePasswordSuccess.observe(this, {
            binding.lottieSuccess.playAnimation {
                findNavController().navigateUp()
            }
        })
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<ChangePasswordViewModel> = ChangePasswordViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_change_password

    override fun getToolbar(): MaterialToolbar = binding.toolbarLayout.defaultToolbar

}