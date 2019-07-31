package com.km.parceltracker.ui.parcels.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.parceltracker.R
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.model.Parcel
import kotlinx.android.synthetic.main.item_parcel.view.*
import java.text.SimpleDateFormat

class ParcelsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        parcel: Parcel,
        onParcelClick: (Parcel) -> Unit,
        onEditParcelClick: (Parcel) -> Unit,
        onDeleteParcelClick: (Parcel) -> Unit
    ) {
        itemView.tvTitle.text = parcel.title
        itemView.tvSender.text = parcel.sender
        itemView.tvCourier.text = parcel.courier
        itemView.tvStatus.text = itemView.context.getString(parcel.parcelStatus.status.statusResId)
        itemView.tvLastUpdated.text = SimpleDateFormat.getDateTimeInstance().format(parcel.lastUpdated)

        when (parcel.parcelStatus.status) {
            ParcelStatusEnum.SENT -> itemView.clItemParcel.setBackgroundColor(itemView.context.getColor(R.color.colorSent))
            ParcelStatusEnum.ORDERED -> itemView.clItemParcel.setBackgroundColor(itemView.context.getColor(R.color.colorOrdered))
            ParcelStatusEnum.DELIVERED -> itemView.clItemParcel.setBackgroundColor(itemView.context.getColor(R.color.colorDelivered))
        }

        itemView.setOnClickListener { onParcelClick(parcel) }
        itemView.ivEdit.setOnClickListener { onEditParcelClick(parcel) }
        itemView.ivDelete.setOnClickListener { onDeleteParcelClick(parcel) }
    }

}