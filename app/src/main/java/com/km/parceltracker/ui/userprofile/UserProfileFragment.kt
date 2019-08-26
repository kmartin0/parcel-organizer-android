package com.km.parceltracker.ui.userprofile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseMVVMFragment
import com.km.parceltracker.databinding.FragmentUserProfileBinding
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : BaseMVVMFragment<FragmentUserProfileBinding, UserProfileViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        btnChangeProfile.setOnClickListener { findNavController().navigate(R.id.action_userProfileFragment_to_updateProfileFragment) }
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<UserProfileViewModel> = UserProfileViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_user_profile

    override fun onStart() {
        super.onStart()
        viewModel.refreshUser()
    }

}