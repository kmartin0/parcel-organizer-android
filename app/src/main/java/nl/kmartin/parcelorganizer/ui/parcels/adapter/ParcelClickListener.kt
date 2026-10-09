package nl.kmartin.parcelorganizer.ui.parcels.adapter

import nl.kmartin.parcelorganizer.model.Parcel

interface ParcelClickListener {
    fun onParcelClick(parcel: Parcel)
    fun onEditParcelClick(parcel: Parcel)
    fun onDeleteParcelClick(parcel: Parcel)
    fun onShareParcelClick(parcel: Parcel)
}