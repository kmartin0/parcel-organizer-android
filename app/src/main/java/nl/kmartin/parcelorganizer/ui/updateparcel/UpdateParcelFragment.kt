package nl.kmartin.parcelorganizer.ui.updateparcel

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import nl.kmartin.parcelorganizer.R
import nl.kmartin.parcelorganizer.base.BaseMVVMFragment
import nl.kmartin.parcelorganizer.databinding.FragmentUpdateParcelBinding
import nl.kmartin.parcelorganizer.enums.ParcelStatusEnum
import nl.kmartin.parcelorganizer.util.playAnimation

class UpdateParcelFragment :
    BaseMVVMFragment<FragmentUpdateParcelBinding, UpdateParcelViewModel>() {

    private val args: UpdateParcelFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        // Set the parcel status dropdown values using ParcelStatusEnum.
        val parcelStatusList = ParcelStatusEnum.entries.map { requireContext().getString(it.stringResId) }
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, parcelStatusList)
        binding.parcelFormLayout.dropdownStatus.setAdapter(adapter)
    }

    private fun initObservers() {
        // Populate the parcel form using the parcel retrieved from args
        viewModel.populateParcelForm(args.parcel)

        // When parcel is updated successfully then display a success animation and navigate to the previous fragment.
        viewModel.parcelUpdateSuccess.observe(viewLifecycleOwner, {
            binding.btnUpdateParcel.isClickable = false
            binding.lottieSuccess.playAnimation {
                findNavController().navigateUp()
            }
        })
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<UpdateParcelViewModel> =
        UpdateParcelViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_update_parcel

    override fun getToolbar(): MaterialToolbar = binding.toolbarLayout.defaultToolbar

}