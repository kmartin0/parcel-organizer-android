package com.km.parceltracker.ui.parcels.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.km.parceltracker.R
import com.km.parceltracker.enums.SortOrderEnum
import com.km.parceltracker.ui.parcels.ParcelsViewModel
import kotlinx.android.synthetic.main.bottom_sheet_parcel_sort_order.*


class SortOrderBottomDialogFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: ParcelsViewModel
    private var initialButtonResId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_parcel_sort_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity as AppCompatActivity).get(ParcelsViewModel::class.java)
        setInitialCheckedButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        applySelection()
    }

    private fun applySelection() {
        if (initialButtonResId != rgSorting.checkedRadioButtonId) {
            when (rgSorting.checkedRadioButtonId) {
                R.id.rbAscending -> viewModel.sortOrder.value = SortOrderEnum.ASCENDING
                R.id.rbDescending -> viewModel.sortOrder.value = SortOrderEnum.DESCENDING
            }
        }
    }

    private fun setInitialCheckedButton() {
        rgSorting.check(
            when (viewModel.sortOrder.value) {
                null, SortOrderEnum.ASCENDING -> R.id.rbAscending
                SortOrderEnum.DESCENDING -> R.id.rbDescending
            }
        )
        initialButtonResId = rgSorting.checkedRadioButtonId
    }

}