package com.km.parceltracker.ui.parcels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.km.parceltracker.R
import com.km.parceltracker.model.Parcel

class ParcelsAdapter(
    private val parcels: List<Parcel>,
    private val onParcelClick: (Parcel) -> Unit,
    private val onEditParcelClick: (Parcel) -> Unit
) :
    RecyclerView.Adapter<ParcelsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelsViewHolder {
        return ParcelsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_parcel, parent, false)
        )
    }

    override fun getItemCount(): Int = parcels.size

    override fun onBindViewHolder(holder: ParcelsViewHolder, position: Int) =
        holder.bind(parcels[position], onParcelClick, onEditParcelClick)

}