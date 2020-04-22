package com.km.parcelorganizer.ui.parcels.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
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
        itemView.tvLastUpdated.text =
            SimpleDateFormat.getDateTimeInstance().format(parcel.lastUpdated)

        itemView.clItemParcel.setBackgroundColor(
            itemView.context.getColor(
                when (parcel.parcelStatus.status) {
                    ParcelStatusEnum.ORDERED -> R.color.pi_color_ordered
                    ParcelStatusEnum.SENT -> R.color.pi_color_sent
                    ParcelStatusEnum.DELIVERED -> R.color.pi_color_delivered
                }
            )
        )

        itemView.ivStatus.setImageResource(
            when (parcel.parcelStatus.status) {
                ParcelStatusEnum.ORDERED -> R.drawable.ic_ordered
                ParcelStatusEnum.SENT -> R.drawable.ic_sent
                ParcelStatusEnum.DELIVERED -> R.drawable.ic_delivered
            }
        )

        if (parcel.trackingUrl.isNullOrEmpty()) {
            itemView.ivShare.isEnabled = false
            itemView.ivShare.isFocusable = false
            itemView.ivShare.isClickable = false
            itemView.ivShare.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
        } else {
            itemView.ivShare.isEnabled = true
            itemView.ivShare.isFocusable = true
            itemView.ivShare.isClickable = true
            itemView.ivShare.colorFilter = null
        }

        itemView.setOnClickListener { parcelClickListener.onParcelClick(parcel) }
        itemView.ivEdit.setOnClickListener { parcelClickListener.onEditParcelClick(parcel) }
        itemView.ivDelete.setOnClickListener { parcelClickListener.onDeleteParcelClick(parcel) }
        itemView.ivShare.setOnClickListener { parcelClickListener.onShareParcelClick(parcel) }
    }

}