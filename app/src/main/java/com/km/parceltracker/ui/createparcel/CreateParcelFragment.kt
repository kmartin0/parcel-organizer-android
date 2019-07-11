package com.km.parceltracker.ui.createparcel

import androidx.appcompat.widget.Toolbar
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseFragment
import kotlinx.android.synthetic.main.toolbar_default.*

class CreateParcelFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_create_parcel

    override fun getToolbar(): Toolbar? = defaultToolbar

    override fun getMenuId(): Int? = null

}