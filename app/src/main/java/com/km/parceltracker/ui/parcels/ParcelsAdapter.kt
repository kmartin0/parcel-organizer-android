package com.km.parceltracker.ui.parcels

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.km.parceltracker.R
import com.km.parceltracker.model.Parcel
import kotlin.collections.ArrayList

class ParcelsAdapter(
    /*private val parcels: MutableList<Parcel>,*/
    private val onParcelClick: (Parcel) -> Unit,
    private val onEditParcelClick: (Parcel) -> Unit
) : RecyclerView.Adapter<ParcelsViewHolder>(), Filterable {

    private val parcels = ArrayList<Parcel>()
    private val filteredParcels = ArrayList<Parcel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelsViewHolder {
        return ParcelsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_parcel, parent, false)
        )
    }

    override fun getItemCount(): Int = filteredParcels.size

    override fun onBindViewHolder(holder: ParcelsViewHolder, position: Int) =
        holder.bind(filteredParcels[position], onParcelClick, onEditParcelClick)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredList = ArrayList<Parcel>()

                if (constraint.isNullOrBlank()) filteredList.addAll(parcels)
                else filteredList.addAll(parcels.filter {
                    it.title.toLowerCase().contains(constraint.toString().toLowerCase())
                })

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredParcels.clear()
                filteredParcels.addAll(results?.values as ArrayList<Parcel>)
                notifyDataSetChanged()
            }
        }
    }

    fun setData(parcels: ArrayList<Parcel>) {
        this.parcels.clear()
        this.parcels.addAll(parcels)

        this.filteredParcels.clear()
        this.filteredParcels.addAll(parcels)

        notifyDataSetChanged()
    }
}