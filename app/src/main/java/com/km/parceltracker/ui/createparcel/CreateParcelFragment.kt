package com.km.parceltracker.ui.createparcel

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseMVVMFragment
import com.km.parceltracker.databinding.FragmentCreateParcelBinding
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.model.ParcelStatus
import kotlinx.android.synthetic.main.form_parcel.*
import kotlinx.android.synthetic.main.toolbar_default.*

class CreateParcelFragment : BaseMVVMFragment<FragmentCreateParcelBinding, CreateParcelViewModel>() {

    private val args: CreateParcelFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        val parcelStatusList = ParcelStatusEnum.values().map { context!!.getString(it.statusResId) }
        val adapter = ArrayAdapter(context!!, R.layout.dropdown_menu_popup_item, parcelStatusList)
        dropdownStatus.setAdapter(adapter)
    }

    private fun initObservers() {
        viewModel.setTrackingUrl(args.trackingUrl)

        viewModel.parcelCreatedSuccess.observe(this, Observer {
            Toast.makeText(context!!, getString(R.string.parcel_created), Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_create_parcel

    override fun getToolbar(): MaterialToolbar? = defaultToolbar

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<CreateParcelViewModel> = CreateParcelViewModel::class.java

}