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
import com.km.parceltracker.repository.UserRepository
import com.km.parceltracker.ui.parcels.adapter.ParcelsAdapter
import com.km.parceltracker.ui.parcels.adapter.ParcelsItemDecoration
import kotlinx.android.synthetic.main.fragment_parcels.*
import kotlinx.android.synthetic.main.toolbar_default.*

/**
 * TODO: Refactor ParcelsViewModel
 * TODO: HTTP Basic
 * TODO: Create Parcel Api
 * TODO: Edit Parcel Api
 * TODO: Remove Parcel Api
 * TODO: Bottom nav bar with user profile
 * TODO: Maybe look into converting shared prefs into Single
 * TODO: Dependency Injection (Koin/Dagger)
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
        // Initialize the recycler view adapter, item decoration and layout manager.
        rvParcels.adapter = parcelsAdapter
        rvParcels.addItemDecoration(ParcelsItemDecoration(context!!))
        rvParcels.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        // When the floating action button is clicked navigate to CreateParcelFragment
        fabCreateParcel.setOnClickListener { onCreateParcelClick() }

        srlParcels.setOnRefreshListener {
            if (viewModel.isLoading.value == false) {
                removeObservers()
                viewModel.refresh()
                initObservers()
            }
            srlParcels.isRefreshing = false
        }

        // Dummy logout button
        fabLogout.setOnClickListener { logout() }
    }

    private fun removeObservers() {
        viewModel.apply {
            parcels.removeObservers(this@ParcelsFragment)
            sortAndFilterConfig.removeObservers(this@ParcelsFragment)
            error.removeObservers(this@ParcelsFragment)
        }
    }

    private fun initObservers() {
        // When parcels list from [viewModel] is changed then clear replace the items from the recycler view with it.
        viewModel.parcels.observe(this, Observer {
            parcels.clear()
            if (it != null) parcels.addAll(it)
            parcelsAdapter.notifyDataSetChanged()
        })

        // Update the menu titles when the sort and filter configuration is changed.
        viewModel.sortAndFilterConfig.observe(this, Observer {
            updateMenuTitles()
        })

        // Display the error message as a toast.
        viewModel.error.observe(this, Observer {
            if (!it.isNullOrBlank()) Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * Navigate to CreateParcelFragment.
     */
    private fun onCreateParcelClick() {
        findNavController().navigate(R.id.action_parcelsFragment_to_createParcelFragment)
    }

    /**
     * Open a Chrome Custom Tabs using the tracking url. If the url is not valid display a toast message.
     */
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

    /**
     * Navigate to UpdateParcelFragment with [parcel] as args.
     */
    private fun onEditParcelClick(parcel: Parcel) {
        val action = ParcelsFragmentDirections.actionParcelsFragmentToUpdateParcelFragment(parcel)
        findNavController().navigate(action)
    }

    /**
     * Open an alert dialog prompting if the user is sure he wants to delete the parcel.
     * when the user clicks yes then the parcel is deleted using the [viewModel].
     */
    private fun onDeleteClick(parcel: Parcel) {
        AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_delete_parcel_title, parcel.title))
            .setMessage(getString(R.string.dialog_delete_parcel_message, parcel.title))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> viewModel.deleteParcel(parcel) }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    /**
     * Initialize the search view and menu titles.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        initSearchView()
        updateMenuTitles()
    }

    /**
     * Initialize the search view by setting the query text listener.
     */
    private fun initSearchView() {
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(getSearchViewQueryListener())
    }

    /**
     * @return [SearchView.OnQueryTextListener] A query text listener which listens to a search view.
     */
    private fun getSearchViewQueryListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { // Do nothing on submit.
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean { // When the query changes, use the viewModel to filter the list using the query.
                viewModel.sortAndFilterConfig.value = viewModel.sortAndFilterConfig.value
                    ?.apply {
                        searchQuery = newText
                    }
                return true
            }

        }
    }

    /**
     * Update the menu titles with the selected sort and filter configuration.
     */
    private fun updateMenuTitles() {
        menu?.let { menu ->
            viewModel.sortAndFilterConfig.value?.let {
                menu.findItem(R.id.sortByBottomDialogFragment)?.title =
                    getString(R.string.sort_by, getString(it.sortBy.stringResId))

                menu.findItem(R.id.sortOrderBottomDialogFragment)?.title =
                    getString(R.string.sort_order, getString(it.sortOrder.stringResId))

                menu.findItem(R.id.searchByBottomDialogFragment)?.title =
                    getString(R.string.search_by, getString(it.searchBy.stringResId))

                menu.findItem(R.id.action_ordered)?.isChecked = it.ordered
                menu.findItem(R.id.action_sent)?.isChecked = it.sent
                menu.findItem(R.id.action_delivered)?.isChecked = it.delivered
            }
        }
    }

    /**
     * Listen for options item selections.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ordered -> { // Invert the ordered checkbox and change it in the sort and filter config
                item.isChecked = !item.isChecked
                viewModel.sortAndFilterConfig.value?.let {
                    it.ordered = item.isChecked
                    viewModel.setSortingAndFilterConfig(it)
                }
                true
            }
            R.id.action_sent -> { // Invert the sent checkbox and change it in the sort and filter config
                item.isChecked = !item.isChecked
                viewModel.sortAndFilterConfig.value?.let {
                    it.sent = item.isChecked
                    viewModel.setSortingAndFilterConfig(it)
                }
                true
            }
            R.id.action_delivered -> { // Invert the delivered checkbox and change it in the sort and filter config
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