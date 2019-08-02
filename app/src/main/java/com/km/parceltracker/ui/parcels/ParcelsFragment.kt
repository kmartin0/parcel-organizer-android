package com.km.parceltracker.ui.parcels

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
 * TODO: Login Api
 * TODO: OAuth2 Api
 * TODO: Register Api
 * TODO: Get parcels Api
 * TODO: Create Parcel Api
 * TODO: Edit Parcel Api
 * TODO: Remove Parcel Api
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
        fabCreateParcel.setOnClickListener { onCreateParcelClick() }
    }

    private fun initObservers() {
        viewModel.parcels.observe(this, Observer {
            parcels.clear()
            if (it != null) parcels.addAll(it)
            parcelsAdapter.notifyDataSetChanged()
        })
        viewModel.sortAndFilterConfig.observe(this, Observer {
            updateMenuTitles()
        })
    }

    private fun onCreateParcelClick() {
        findNavController().navigate(R.id.action_parcelsFragment_to_createParcelFragment)
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
        val action = ParcelsFragmentDirections.actionParcelsFragmentToUpdateParcelFragment(parcel)
        findNavController().navigate(action)
    }

    private fun onDeleteClick(parcel: Parcel) {
        AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_delete_parcel_title, parcel.title))
            .setMessage(getString(R.string.dialog_delete_parcel_message, parcel.title))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> viewModel.deleteParcel(parcel) }
            .setNegativeButton(getString(R.string.no), null)
            .show()
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
                viewModel.sortAndFilterConfig.value = viewModel.sortAndFilterConfig.value
                    ?.apply {
                        searchQuery = newText
                    }
                return true
            }

        }
    }

    private fun updateMenuTitles() {
        menu?.let { menu ->
            viewModel.sortAndFilterConfig.value?.let {
                menu.findItem(R.id.sortByBottomDialogFragment)?.title = getString(R.string.sort_by, it.sortBy.value)
                menu.findItem(R.id.sortOrderBottomDialogFragment)?.title =
                    getString(R.string.sort_order, it.sortOrder.order)
                menu.findItem(R.id.searchByBottomDialogFragment)?.title =
                    getString(R.string.search_by, it.searchBy.value)
                menu.findItem(R.id.action_ordered)?.isChecked = it.ordered
                menu.findItem(R.id.action_sent)?.isChecked = it.sent
                menu.findItem(R.id.action_delivered)?.isChecked = it.delivered
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ordered -> {
                item.isChecked = !item.isChecked
                viewModel.sortAndFilterConfig.value?.let {
                    it.ordered = item.isChecked
                    viewModel.setSortingAndFilterConfig(it)
                }
                true
            }
            R.id.action_sent -> {
                item.isChecked = !item.isChecked
                viewModel.sortAndFilterConfig.value?.let {
                    it.sent = item.isChecked
                    viewModel.setSortingAndFilterConfig(it)
                }
                true
            }
            R.id.action_delivered -> {
                item.isChecked = !item.isChecked
                viewModel.sortAndFilterConfig.value?.let {
                    it.delivered = item.isChecked
                    viewModel.setSortingAndFilterConfig(it)
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