package com.km.parceltracker.ui.parcels.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.parceltracker.R

class ParcelsItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val spacing = context.resources.getDimension(R.dimen.default_padding)
        val fabSize = context.resources.getDimension(R.dimen.normal_fab_size)

        outRect.top = spacing.toInt()
        outRect.bottom =
            if (position == state.itemCount.minus(1)) spacing.toInt().plus(fabSize.toInt()) else spacing.toInt()
        outRect.left = spacing.toInt()
        outRect.right = spacing.toInt()
    }

}