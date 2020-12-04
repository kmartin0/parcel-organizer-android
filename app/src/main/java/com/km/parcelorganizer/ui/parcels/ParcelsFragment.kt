package com.km.parcelorganizer.ui.parcels

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.km.parcelorganizer.R
import com.km.parcelorganizer.base.BaseMVVMFragment
import com.km.parcelorganizer.databinding.FragmentParcelsBinding
import com.km.parcelorganizer.model.Parcel
import com.km.parcelorganizer.ui.MainActivity
import com.km.parcelorganizer.ui.parcels.adapter.ParcelClickListener
import com.km.parcelorganizer.ui.parcels.adapter.ParcelsAdapter
import com.km.parcelorganizer.ui.parcels.adapter.ParcelsItemDecoration
import kotlinx.android.synthetic.main.fragment_parcels.*
import kotlinx.android.synthetic.main.toolbar_default.*

/**
 * TODO: Refactor: Move the handle api error in BaseViewModel code someplace else.
 * TODO: Fix: navigating temporarily displays next toolbar before transition is complete.
 * TODO: Fix: Going from parcels to add the bottom nav bar goes away too fast.
 * TODO: Fix: Loading indicator not working properly with multiple calls/tasks.
 * TODO: Fix: Null pointer Single.fromCallable ParcelsViewModel
 * TODO: Refactor: Session expired message instead of login failed.
 * TODO: Refactor: Maybe look into converting shared prefs into Single (This might solve i.e. the refreshLoggedInUserSituation in ParcelsFragment and UserProfileFragment)
 * TODO: Add: Dependency injection (making repositories Singleton)
 */
class ParcelsFragment : BaseMVVMFragment<FragmentParcelsBinding, ParcelsViewModel>(),
    ParcelClickListener {

    private val args: ParcelsFragmentArgs by navArgs()
    private var isArgsConsumed = false

    private val parcels = ArrayList<Parcel>()
    private val parcelsAdapter = ParcelsAdapter(parcels, this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkRedirectCreateParcel()
        initViews()
        initObservers()
        setToolbarLogo()
    }

    private fun setToolbarLogo() {
        (requireActivity() as MainActivity?)?.supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_box)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun checkRedirectCreateParcel() {
        if (!isArgsConsumed) {
            args.trackingUrl?.let {
                findNavController().navigate(
                    ParcelsFragmentDirections.actionParcelsFragmentToCreateParcelFragment(
                        it
                    )
                )
                isArgsConsumed = true
            }
        }
    }

    private fun initViews() {
        // Initialize the recycler view adapter, item decoration and layout manager.
        rvParcels.adapter = parcelsAdapter
        rvParcels.addItemDecoration(ParcelsItemDecoration(requireContext()))
        rvParcels.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        // When the floating action button is clicked navigate to CreateParcelFragment
        fabCreateParcel.setOnClickListener { onCreateParcelClick() }

        srlParcels.setOnRefreshListener {
            viewModel.refreshParcels()
            srlParcels.isRefreshing = false
        }
    }

    private fun initObservers() {
        // When parcels list from [viewModel] is changed then clear replace the items from the recycler view with it.
        viewModel.parcels.observe(viewLifecycleOwner, {
            parcels.clear()
            if (it != null) parcels.addAll(it)
            setEmptyStateVisibility()
            parcelsAdapter.notifyDataSetChanged()
        })

        // Update the menu titles when the sort and filter configuration is changed.
        viewModel.sortAndFilterConfig.observe(viewLifecycleOwner, {
            updateMenuTitles()
        })

        // When parcels are being fetched hide empty state
        viewModel.startLoadingParcels.observe(this, {
            parcelsEmptyStateView.visibility = View.INVISIBLE
        })
    }

    private fun setEmptyStateVisibility() {
        parcelsEmptyStateView.visibility =
            if (parcels.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
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
    override fun onParcelClick(parcel: Parcel) {
        val trackingUrl = parcel.parseTrackingUrl()
        if (!trackingUrl.isNullOrEmpty()) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(trackingUrl))
        } else {
            Toast.makeText(
                context,
                getString(R.string.provide_valid_url),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Navigate to UpdateParcelFragment with [parcel] as args.
     */
    override fun onEditParcelClick(parcel: Parcel) {
        val action = ParcelsFragmentDirections.actionParcelsFragmentToUpdateParcelFragment(parcel)
        findNavController().navigate(action)
    }

    /**
     * Open an alert dialog prompting if the user is sure he wants to delete the parcel.
     * when the user clicks yes then the parcel is deleted using the [viewModel].
     */
    override fun onDeleteParcelClick(parcel: Parcel) {
        if (viewModel.isLoading.value == false) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_delete_parcel_title, parcel.title))
                .setMessage(getString(R.string.dialog_delete_parcel_message, parcel.title))
                .setPositiveButton(getString(R.string.yes)) { _, _ -> viewModel.deleteParcel(parcel) }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }
    }

    /**
     * Share the parcel tracking url with other apps.
     */
    override fun onShareParcelClick(parcel: Parcel) {
        val trackingUrl = parcel.parseTrackingUrl()
        if (!trackingUrl.isNullOrEmpty()) {
            ShareCompat.IntentBuilder.from(requireActivity())
                .setType("text/plain")
                .setChooserTitle(R.string.share_tracking_url)
                .setText(trackingUrl)
                .startChooser()
        } else {
            Toast.makeText(
                context,
                getString(R.string.provide_valid_url),
                Toast.LENGTH_SHORT
            ).show()
        }
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

    // Used to share the view model with the search and sort dialog fragments.
    override fun isSharedViewModel(): Boolean = true

    override fun onStart() {
        super.onStart()
        viewModel.refreshParcels()
    }

}