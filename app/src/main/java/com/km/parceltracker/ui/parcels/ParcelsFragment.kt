package com.km.parceltracker.ui.parcels

import androidx.appcompat.widget.Toolbar
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_parcels.*

class ParcelsFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_parcels

    override fun getToolbar(): Toolbar = toolbarParcels

    override fun getMenuId(): Int? = R.menu.menu_parcels

}