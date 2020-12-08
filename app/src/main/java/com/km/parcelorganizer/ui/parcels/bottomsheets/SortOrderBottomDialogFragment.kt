package com.km.parcelorganizer.ui.parcels.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.km.parcelorganizer.R
import com.km.parcelorganizer.databinding.BottomSheetParcelSortOrderBinding
import com.km.parcelorganizer.enums.SortOrderEnum
import com.km.parcelorganizer.ui.parcels.ParcelsViewModel

class SortOrderBottomDialogFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: ParcelsViewModel
    private lateinit var binding: BottomSheetParcelSortOrderBinding
    private var initialButtonResId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetParcelSortOrderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(activity as AppCompatActivity).get(ParcelsViewModel::class.java)
        setInitialCheckedButton()
    }

    /**
     * Set the checked state for the a radio button using the sortOrder attribute from sortAndFilterConfig attribute from the [viewModel].
     */
    private fun setInitialCheckedButton() {
        binding.rgSorting.check(
            when (viewModel.sortAndFilterConfig.value?.sortOrder) {
                null, SortOrderEnum.ASCENDING -> R.id.rbAscending
                SortOrderEnum.DESCENDING -> R.id.rbDescending
            }
        )
        initialButtonResId = binding.rgSorting.checkedRadioButtonId
    }

    /**
     * Change the sortOrder attribute from sortAndFilterConfig of the [viewModel] to the value of the selected radio button.
     */
    private fun applySelection() {
        if (initialButtonResId != binding.rgSorting.checkedRadioButtonId) {
            viewModel.sortAndFilterConfig.value?.let {
                it.sortOrder = when (binding.rgSorting.checkedRadioButtonId) {
                    R.id.rbAscending -> SortOrderEnum.ASCENDING
                    R.id.rbDescending -> SortOrderEnum.DESCENDING
                    else -> SortOrderEnum.ASCENDING
                }
                viewModel.setSortingAndFilterConfig(it)
            }
        }
    }

    /**
     * When the bottom dialog is destroyed apply the selection.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        applySelection()
    }

}
