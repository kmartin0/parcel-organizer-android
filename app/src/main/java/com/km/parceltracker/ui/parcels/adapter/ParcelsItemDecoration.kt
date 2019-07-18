package com.km.parceltracker.ui.parcels.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.parceltracker.R

class ParcelsItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spacing = context.resources.getDimension(R.dimen.default_padding)
        outRect.top = spacing.toInt()
        outRect.bottom = spacing.toInt()
        outRect.left = spacing.toInt()
        outRect.right = spacing.toInt()
    }

}