package com.km.parcelorganizer.ui.parcels.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.km.parcelorganizer.R
import com.km.parcelorganizer.model.Parcel

class ParcelsAdapter(
    private val parcels: MutableList<Parcel>,
    private val onParcelClick: (Parcel) -> Unit,
    private val onEditParcelClick: (Parcel) -> Unit,
    private val onDeleteParcelClick: (Parcel) -> Unit
) : RecyclerView.Adapter<ParcelsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelsViewHolder {
        return ParcelsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_parcel, parent, false)
        )
    }

    override fun getItemCount(): Int = parcels.size

    override fun onBindViewHolder(holder: ParcelsViewHolder, position: Int) =
        holder.bind(parcels[position], onParcelClick, onEditParcelClick, onDeleteParcelClick)

}