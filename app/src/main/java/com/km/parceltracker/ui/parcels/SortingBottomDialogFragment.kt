package com.km.parceltracker.ui.parcels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.km.parceltracker.R
import kotlinx.android.synthetic.main.bottom_sheet_parcel_sorting.*

class SortingBottomDialogFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: ParcelsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_parcel_sorting, container, false)
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
        when (rgSorting.checkedRadioButtonId) {
            R.id.rbTitle -> viewModel.sortBy.value = ParcelSortingEnum.TITLE
            R.id.rbSender -> viewModel.sortBy.value = ParcelSortingEnum.SENDER
            R.id.rbCourier -> viewModel.sortBy.value = ParcelSortingEnum.COURIER
            R.id.rbDate -> viewModel.sortBy.value = ParcelSortingEnum.DATE
            R.id.rbStatus -> viewModel.sortBy.value = ParcelSortingEnum.STATUS
        }
    }

    private fun setInitialCheckedButton() {
        rgSorting.check(
            when (viewModel.sortBy.value) {
                ParcelSortingEnum.TITLE -> R.id.rbTitle
                ParcelSortingEnum.SENDER -> R.id.rbSender
                ParcelSortingEnum.COURIER -> R.id.rbCourier
                ParcelSortingEnum.DATE -> R.id.rbDate
                ParcelSortingEnum.STATUS -> R.id.rbStatus
                null -> R.id.rbTitle
            }
        )
    }

}