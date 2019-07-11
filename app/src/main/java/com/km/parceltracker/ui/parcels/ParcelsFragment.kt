package com.km.parceltracker.ui.parcels

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseFragment
import com.km.parceltracker.model.Parcel
import kotlinx.android.synthetic.main.fragment_parcels.*
import kotlinx.android.synthetic.main.toolbar_default.*
import java.util.*
import kotlin.collections.ArrayList

class ParcelsFragment : BaseFragment() {

    private val parcels = ArrayList<Parcel>()
    private val parcelsAdapter =
        ParcelsAdapter(parcels, { onParcelClick(it) }, { onEditParcelClick(it) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        rvParcels.adapter = parcelsAdapter
        rvParcels.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        parcels.apply {
            add(Parcel(1L, "Garmin VivoMove HR", "Bol.com", "PostNL", "https://jouw.postnl.nl/#!/track-en-trace/3STUNN002038759/NL/1185ZC", "DELIVERED", Date()))
            add(Parcel(1L, "Clothing", "Wehkamp", "DHL", null, "SENT", Date()))
            add(Parcel(1L, "Shoes", "Zalando", "DPD", null, "SENT", Date()))
            add(Parcel(1L, "Car", "Mercedes", "Mercedes Delivery Service", null, "ORDERED", Date()))
        }
        parcelsAdapter.notifyDataSetChanged()
    }

    private fun onParcelClick(parcel: Parcel) {
        if (parcel.trackingUrl != null && URLUtil.isValidUrl(parcel.trackingUrl)) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(parcel.trackingUrl))
        } else {
            //TODO: Use Snack bar Action for instant url add dialog popup.
            Toast.makeText(context, "Please provide the parcel with a valid tracking url", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onEditParcelClick(parcel: Parcel) {
        Toast.makeText(context, "onEditParcelClick: ${parcel.title}", Toast.LENGTH_SHORT).show()
    }

    override fun getLayoutId(): Int = R.layout.fragment_parcels

    override fun getToolbar(): Toolbar? = defaultToolbar

    override fun getMenuId(): Int? = R.menu.menu_parcels

}