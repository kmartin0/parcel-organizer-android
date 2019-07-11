package com.km.parceltracker.base

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.km.parceltracker.R

abstract class BaseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Set has options menu to true if the fragment has a menu id.
        if (getMenuId() != null) setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = getToolbar()

        // Attach the toolbar with he navigation graph.
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        // Set the support action bar.
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    /**
     * Displays a loading indicator based on [visibility]
     */
    fun showLoading(visibility: Boolean) {
        activity?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    // Inflate the menu for this fragment.
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuId()?.let {
            inflater?.inflate(it, menu)
        }
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getToolbar(): Toolbar

    @MenuRes
    abstract fun getMenuId(): Int?
}
