package com.km.parceltracker.ui.parcels

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.parceltracker.R
import com.km.parceltracker.model.Parcel
import kotlinx.android.synthetic.main.item_parcel.view.*
import java.text.SimpleDateFormat

class ParcelsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(parcel: Parcel, onParcelClick: (Parcel) -> Unit, onEditParcelClick: (Parcel) -> Unit) {
        itemView.tvTitle.text = parcel.title
        itemView.tvSender.text = parcel.sender
        itemView.tvCourier.text = parcel.courier
        itemView.tvStatus.text = parcel.parcelStatus
        itemView.tvLastUpdated.text = SimpleDateFormat.getDateTimeInstance().format(parcel.lastUpdated)

        when (parcel.parcelStatus) {
            "SENT" -> itemView.clItemParcel.setBackgroundColor(itemView.context.getColor(R.color.colorAccent))
            "ORDERED" -> itemView.clItemParcel.setBackgroundColor(itemView.context.getColor(R.color.colorDivider))
            "DELIVERED" -> itemView.clItemParcel.setBackgroundColor(itemView.context.getColor(R.color.lightGreen))
        }

        itemView.setOnClickListener { onParcelClick(parcel) }
        itemView.ivEdit.setOnClickListener { onEditParcelClick(parcel) }
    }

}