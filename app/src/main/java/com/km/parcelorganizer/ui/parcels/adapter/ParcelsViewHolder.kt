package com.km.parcelorganizer.ui.parcels.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.parcelorganizer.R
import com.km.parcelorganizer.databinding.ItemParcelBinding
import com.km.parcelorganizer.enums.ParcelStatusEnum
import com.km.parcelorganizer.model.Parcel
import com.km.parcelorganizer.util.getColorFromAttr
import java.text.SimpleDateFormat

class ParcelsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Bind the data to the view.
     */
    fun bind(
        parcel: Parcel,
        parcelClickListener: ParcelClickListener
    ) {
        val binding = ItemParcelBinding.bind(itemView)

        setBaseInfo(binding, parcel)
        setBackgroundColor(binding, parcel)
        setStatusIcon(binding, parcel)
        setShare(binding, parcel)
        setClickListeners(binding, parcel, parcelClickListener)
    }

    private fun setBaseInfo(
        binding: ItemParcelBinding,
        parcel: Parcel
    ) {
        with(binding) {
            tvTitle.text = parcel.title
            tvSender.text = parcel.sender
            tvCourier.text = parcel.courier
            tvStatus.text =
                itemView.context.getString(parcel.parcelStatus.status.stringResId)
            tvAdditionalInformation.text = parcel.additionalInformation
            tvLastUpdated.text =
                SimpleDateFormat.getDateTimeInstance().format(parcel.lastUpdated)
        }
    }

    private fun setBackgroundColor(
        binding: ItemParcelBinding,
        parcel: Parcel
    ) {
        with(binding) {
            clItemParcel.setBackgroundColor(
                itemView.context.getColorFromAttr(
                    when (parcel.parcelStatus.status) {
                        ParcelStatusEnum.ORDERED -> R.attr.piColorOrdered
                        ParcelStatusEnum.SENT -> R.attr.piColorSent
                        ParcelStatusEnum.DELIVERED -> R.attr.piColorDelivered
                    }
                )
            )
        }
    }

    private fun setStatusIcon(
        binding: ItemParcelBinding,
        parcel: Parcel
    ) {
        with(binding) {
            ivStatus.setImageResource(
                when (parcel.parcelStatus.status) {
                    ParcelStatusEnum.ORDERED -> R.drawable.ic_ordered
                    ParcelStatusEnum.SENT -> R.drawable.ic_sent
                    ParcelStatusEnum.DELIVERED -> R.drawable.ic_delivered
                }
            )
        }
    }

    private fun setShare(
        binding: ItemParcelBinding,
        parcel: Parcel
    ) {
        with(binding) {
            // Sets the tracking url view
            if (parcel.trackingUrl.isNullOrEmpty()) {
                ivShare.isEnabled = false
                ivShare.isFocusable = false
                ivShare.isClickable = false
                ivShare.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
            } else {
                ivShare.isEnabled = true
                ivShare.isFocusable = true
                ivShare.isClickable = true
                ivShare.colorFilter = null
            }
        }
    }

    private fun setClickListeners(
        binding: ItemParcelBinding,
        parcel: Parcel,
        listener: ParcelClickListener
    ) {
        with(binding) {
            root.setOnClickListener { listener.onParcelClick(parcel) }
            ivEdit.setOnClickListener { listener.onEditParcelClick(parcel) }
            ivDelete.setOnClickListener { listener.onDeleteParcelClick(parcel) }
            ivShare.setOnClickListener { listener.onShareParcelClick(parcel) }
        }
    }

}