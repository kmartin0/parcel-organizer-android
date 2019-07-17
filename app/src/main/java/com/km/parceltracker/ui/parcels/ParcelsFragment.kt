package com.km.parceltracker.ui.parcels

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseMVVMFragment
import com.km.parceltracker.databinding.FragmentParcelsBinding
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.ui.parcels.adapter.ParcelsAdapter
import kotlinx.android.synthetic.main.fragment_parcels.*
import kotlinx.android.synthetic.main.toolbar_default.*

/**
 * TODO: SORT BY: title, date, courier, sender, value :)
 * TODO: FILTER BY: value :)
 * TODO: SEARCH BY: title, courier, sender
 * TODO: SORT ORDER: sortOrder, descending :)
 */
class ParcelsFragment : BaseMVVMFragment<FragmentParcelsBinding, ParcelsViewModel>() {

    private val parcels = ArrayList<Parcel>()
    private val parcelsAdapter =
        ParcelsAdapter(
            parcels,
            { onParcelClick(it) },
            { onEditParcelClick(it) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        rvParcels.adapter = parcelsAdapter
        rvParcels.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun initObservers() {
        viewModel.parcels.observe(activity as AppCompatActivity, Observer {
            parcels.clear()
            if (it != null) parcels.addAll(it)
            parcelsAdapter.notifyDataSetChanged()
        })
        viewModel.sortBy.observe(activity as AppCompatActivity, Observer {
            updateMenuTitles()
        })

        viewModel.sortOrder.observe(activity as AppCompatActivity, Observer {
            updateMenuTitles()
        })
        viewModel.searchBy.observe(activity as AppCompatActivity, Observer {
            updateMenuTitles()
        })
    }

    private fun onParcelClick(parcel: Parcel) {
        if (parcel.trackingUrl != null && URLUtil.isValidUrl(parcel.trackingUrl)) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(parcel.trackingUrl))
        } else {
            //TODO: Use Snack bar Action for instant url add dialog popup.
            Toast.makeText(context, "Please provide the parcel with a valid tracking url", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onEditParcelClick(parcel: Parcel) {
        Toast.makeText(context, "onEditParcelClick: ${parcel.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        initSearchView()
        updateMenuTitles()
    }

    private fun initSearchView() {
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(getSearchViewQueryListener())
    }

    private fun getSearchViewQueryListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText
                return true
            }

        }
    }

    private fun updateMenuTitles() {
        menu?.let { menu ->
            viewModel.sortBy.value?.let {
                menu.findItem(R.id.sortByBottomDialogFragment)?.title = getString(R.string.sort_by, it.value)
            }
            viewModel.sortOrder.value?.let {
                menu.findItem(R.id.sortOrderBottomDialogFragment)?.title = getString(R.string.sort_order, it.order)
            }
            viewModel.searchBy.value?.let {
                menu.findItem(R.id.searchByBottomDialogFragment)?.title = getString(R.string.search_by, it.value)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ordered -> {
                item.isChecked = false
                true
            }
            R.id.action_sent -> {
                item.isChecked = false
                true
            }
            R.id.action_delivered -> {
                item.isChecked = false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_parcels

    override fun getToolbar(): Toolbar? = defaultToolbar

    override fun getMenuId(): Int? = R.menu.menu_parcels

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<ParcelsViewModel> = ParcelsViewModel::class.java

    override fun isSharedViewModel(): Boolean = true

}