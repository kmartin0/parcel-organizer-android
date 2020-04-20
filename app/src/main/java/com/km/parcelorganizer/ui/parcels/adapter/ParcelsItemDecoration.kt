package com.km.parcelorganizer.ui.parcels.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.parcelorganizer.R

class ParcelsItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    /**
     * Set the spacing between parcel items. Add extra space to the bottom of the last item so that the last item
     * won't be covered by a floating action button.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val spacingHorizontal = context.resources.getDimension(R.dimen.key_line_5)
        val spacingVertical = context.resources.getDimension(R.dimen.default_padding)
        val fabSize = context.resources.getDimension(R.dimen.normal_fab_size)

        outRect.top = spacingVertical.toInt()
        outRect.bottom =
            if (position == state.itemCount.minus(1)) spacingVertical.toInt().plus(fabSize.toInt()) else spacingVertical.toInt()
        outRect.left = spacingHorizontal.toInt()
        outRect.right = spacingHorizontal.toInt()
    }

}