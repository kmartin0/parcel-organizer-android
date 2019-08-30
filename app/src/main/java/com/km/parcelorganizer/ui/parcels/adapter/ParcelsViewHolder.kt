package com.km.parcelorganizer.ui.parcels.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.parcelorganizer.R
import com.km.parcelorganizer.enums.ParcelStatusEnum
import com.km.parcelorganizer.model.Parcel
import kotlinx.android.synthetic.main.item_parcel.view.*
import java.text.SimpleDateFormat

class ParcelsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Bind the data to the view.
     */
    fun bind(
        parcel: Parcel,
        parcelClickListener: ParcelClickListener
    ) {
        itemView.tvTitle.text = parcel.title
        itemView.tvSender.text = parcel.sender
        itemView.tvCourier.text = parcel.courier
        itemView.tvStatus.text = itemView.context.getString(parcel.parcelStatus.status.stringResId)
        itemView.tvLastUpdated.text = SimpleDateFormat.getDateTimeInstance().format(parcel.lastUpdated)

        when (parcel.parcelStatus.status) {
            ParcelStatusEnum.SENT -> itemView.clItemParcel.setBackgroundColor(itemView.context.getColor(R.color.colorSent))
            ParcelStatusEnum.ORDERED -> itemView.clItemParcel.setBackgroundColor(itemView.context.getColor(R.color.colorOrdered))
            ParcelStatusEnum.DELIVERED -> itemView.clItemParcel.setBackgroundColor(itemView.context.getColor(R.color.colorDelivered))
        }

        itemView.setOnClickListener { parcelClickListener.onParcelClick(parcel) }
        itemView.ivEdit.setOnClickListener { parcelClickListener.onEditParcelClick(parcel) }
        itemView.ivDelete.setOnClickListener { parcelClickListener.onDeleteParcelClick(parcel) }
        itemView.ivShare.setOnClickListener { parcelClickListener.onShareParcelClick(parcel) }
    }

}