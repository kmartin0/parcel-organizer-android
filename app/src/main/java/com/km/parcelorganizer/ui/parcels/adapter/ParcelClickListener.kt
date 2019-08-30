package com.km.parcelorganizer.ui.parcels.adapter

import com.km.parcelorganizer.model.Parcel

interface ParcelClickListener {
    fun onParcelClick(parcel: Parcel)
    fun onEditParcelClick(parcel: Parcel)
    fun onDeleteParcelClick(parcel: Parcel)
    fun onShareParcelClick(parcel: Parcel)
}