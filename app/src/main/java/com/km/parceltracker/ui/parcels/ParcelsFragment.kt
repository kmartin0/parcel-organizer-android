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
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseMVVMFragment
import com.km.parceltracker.databinding.FragmentParcelsBinding
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.ui.parcels.adapter.ParcelsAdapter
import com.km.parceltracker.ui.parcels.adapter.ParcelsItemDecoration
import kotlinx.android.synthetic.main.fragment_parcels.*
import kotlinx.android.synthetic.main.toolbar_default.*

/**
 * TODO: Login
 * TODO: OAuth2
 * TODO: Register
 * TODO: Create Parcel
 * TODO: Create Parcel from share
 * TODO: Edit Parcel
 * TODO: Remove Parcel
 * TODO: Cache sort and filter configuration
 */
class ParcelsFragment : BaseMVVMFragment<FragmentParcelsBinding, ParcelsViewModel>() {

    private val parcels = ArrayList<Parcel>()
    private val parcelsAdapter =
        ParcelsAdapter(
            parcels,
            { onParcelClick(it) },
            { onEditParcelClick(it) },
            { onDeleteClick(it) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        rvParcels.adapter = parcelsAdapter
        rvParcels.addItemDecoration(ParcelsItemDecoration(context!!))
        rvParcels.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun initObservers() {
        viewModel.parcels.observe(this, Observer {
            parcels.clear()
            if (it != null) parcels.addAll(it)
            parcelsAdapter.notifyDataSetChanged()
        })
        viewModel.sortAndFilterSelection.observe(this, Observer {
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

    private fun onDeleteClick(parcel: Parcel) {
        Toast.makeText(context, "onDeleteClick: ${parcel.title}", Toast.LENGTH_SHORT).show()
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
                viewModel.sortAndFilterSelection.value = viewModel.sortAndFilterSelection.value
                    ?.apply {
                        searchQuery = newText
                    }
                return true
            }

        }
    }

    private fun updateMenuTitles() {
        menu?.let { menu ->
            viewModel.sortAndFilterSelection.value?.let {
                menu.findItem(R.id.sortByBottomDialogFragment)?.title = getString(R.string.sort_by, it.sortBy.value)
            }
            viewModel.sortAndFilterSelection.value?.let {
                menu.findItem(R.id.sortOrderBottomDialogFragment)?.title =
                    getString(R.string.sort_order, it.sortOrder.order)
            }
            viewModel.sortAndFilterSelection.value?.let {
                menu.findItem(R.id.searchByBottomDialogFragment)?.title =
                    getString(R.string.search_by, it.searchBy.value)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ordered -> {
                item.isChecked = !item.isChecked
                viewModel.sortAndFilterSelection.value = viewModel.sortAndFilterSelection.value
                    ?.apply {
                        ordered = item.isChecked
                    }
                true
            }
            R.id.action_sent -> {
                item.isChecked = !item.isChecked
                viewModel.sortAndFilterSelection.value = viewModel.sortAndFilterSelection.value
                    ?.apply {
                        sent = item.isChecked
                    }
                true
            }
            R.id.action_delivered -> {
                item.isChecked = !item.isChecked
                viewModel.sortAndFilterSelection.value = viewModel.sortAndFilterSelection.value
                    ?.apply {
                        delivered = item.isChecked
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_parcels

    override fun getToolbar(): MaterialToolbar? = defaultToolbar

    override fun getMenuId(): Int? = R.menu.menu_parcels

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<ParcelsViewModel> = ParcelsViewModel::class.java

    override fun isSharedViewModel(): Boolean = true

}