package com.km.parcelorganizer.ui.updateprofile

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.km.parcelorganizer.R
import com.km.parcelorganizer.base.BaseMVVMFragment
import com.km.parcelorganizer.databinding.FragmentUpdateProfileBinding
import com.km.parcelorganizer.util.playAnimation
import kotlinx.android.synthetic.main.fragment_update_profile.*
import kotlinx.android.synthetic.main.toolbar_default.*

class UpdateProfileFragment :
    BaseMVVMFragment<FragmentUpdateProfileBinding, UpdateProfileViewModel>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.profileUpdateSuccess.observe(this, Observer {
            btnSubmit.isClickable = false
            lottieSuccess.playAnimation {
                findNavController().navigateUp()
            }
        })
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<UpdateProfileViewModel> = UpdateProfileViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_update_profile

    override fun getToolbar(): MaterialToolbar? = defaultToolbar

}