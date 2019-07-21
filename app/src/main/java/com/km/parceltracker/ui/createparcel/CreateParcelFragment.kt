package com.km.parceltracker.ui.createparcel

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseMVVMFragment
import com.km.parceltracker.databinding.FragmentCreateParcelBinding
import com.km.parceltracker.enums.ParcelStatusEnum
import kotlinx.android.synthetic.main.fragment_create_parcel.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.toolbar_default.*

class CreateParcelFragment : BaseMVVMFragment<FragmentCreateParcelBinding, CreateParcelViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        val adapter = ArrayAdapter(context!!, R.layout.dropdown_menu_popup_item, ParcelStatusEnum.values())
        dropdownStatus.setAdapter(adapter)
        dropdownStatus.setOnItemClickListener { parent, view, position, id ->
            viewModel.parcelForm.trackingStatus.value = ParcelStatusEnum.valueOf(dropdownStatus.text.toString())
        }
    }

    private fun initObservers() {
        viewModel.parcelCreatedSuccess.observe(this, Observer {
            Toast.makeText(context!!, getString(R.string.parcel_created), Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_create_parcel

    override fun getToolbar(): MaterialToolbar? = defaultToolbar

    override fun getMenuId(): Int? = null

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<CreateParcelViewModel> = CreateParcelViewModel::class.java

}