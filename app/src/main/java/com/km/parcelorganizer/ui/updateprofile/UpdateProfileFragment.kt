package com.km.parcelorganizer.ui.updateprofile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.km.parcelorganizer.R
import com.km.parcelorganizer.base.BaseMVVMFragment
import com.km.parcelorganizer.databinding.FragmentUpdateProfileBinding
import com.km.parcelorganizer.util.playAnimation

class UpdateProfileFragment :
    BaseMVVMFragment<FragmentUpdateProfileBinding, UpdateProfileViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()

    }

    private fun initObservers() {
        viewModel.profileUpdateSuccess.observe(this, {
            binding.btnSubmit.isClickable = false
            binding.lottieSuccess.playAnimation {
                findNavController().navigateUp()
            }
        })
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<UpdateProfileViewModel> = UpdateProfileViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_update_profile

    override fun getToolbar(): MaterialToolbar = binding.toolbarLayout.defaultToolbar

}