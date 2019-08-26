package com.km.parceltracker.ui.updateprofile

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseMVVMFragment
import com.km.parceltracker.databinding.FragmentUpdateProfileBinding
import com.km.parceltracker.util.playAnimation
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