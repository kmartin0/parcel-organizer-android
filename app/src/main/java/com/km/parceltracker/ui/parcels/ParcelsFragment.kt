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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseMVVMFragment
import com.km.parceltracker.databinding.FragmentParcelsBinding
import com.km.parceltracker.model.Parcel
import kotlinx.android.synthetic.main.fragment_parcels.*
import kotlinx.android.synthetic.main.toolbar_default.*

class ParcelsFragment : BaseMVVMFragment<FragmentParcelsBinding, ParcelsViewModel>() {

    private val parcels = ArrayList<Parcel>()
    private val parcelsAdapter =
        ParcelsAdapter(parcels, { onParcelClick(it) }, { onEditParcelClick(it) })

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
            menu?.findItem(R.id.action_sort)?.title = getString(R.string.sort_by, it.status)
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

        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(getSearchViewQueryListener())

        menu.findItem(R.id.action_sort)?.title = getString(R.string.sort_by, viewModel.sortBy.value?.status)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort -> {
                findNavController().navigate(R.id.sortingBottomDialogFragment)
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getSearchViewQueryListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.query.value = newText
                return true
            }

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