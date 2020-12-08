package com.km.parcelorganizer.ui.createparcel

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import com.km.parcelorganizer.R
import com.km.parcelorganizer.base.BaseMVVMFragment
import com.km.parcelorganizer.databinding.FragmentCreateParcelBinding
import com.km.parcelorganizer.enums.ParcelStatusEnum
import com.km.parcelorganizer.util.playAnimation

class CreateParcelFragment :
    BaseMVVMFragment<FragmentCreateParcelBinding, CreateParcelViewModel>() {

    private val args: CreateParcelFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }


    private fun initViews() {
        // Initialize the options for the Parcel Status dropdown.
        val parcelStatusList = ParcelStatusEnum.values().map { requireContext().getString(it.stringResId) }
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, parcelStatusList)
        binding.parcelFormLayout.dropdownStatus.setAdapter(adapter)
    }

    private fun initObservers() {
        // Set the tracking url from the args.
        viewModel.setTrackingUrl(args.trackingUrl)

        // When a parcel has been created display a success animation and return to the previous fragment.
        viewModel.parcelCreatedSuccess.observe(this, {
            binding.btnCreateParcel.isClickable = false
            binding.lottieSuccess.playAnimation {
                findNavController().navigateUp()
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_create_parcel

    override fun getToolbar(): MaterialToolbar = binding.toolbarLayout.defaultToolbar

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<CreateParcelViewModel> = CreateParcelViewModel::class.java

}