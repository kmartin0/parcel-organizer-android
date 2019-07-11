package com.km.parceltracker.ui.parcels

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_parcels.*
import kotlinx.android.synthetic.main.toolbar_default.*

class ParcelsFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_parcels

    override fun getToolbar(): Toolbar? = defaultToolbar

    override fun getMenuId(): Int? = R.menu.menu_parcels

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
//                getToolbar()?.let {
//            // Attach the toolbar with he navigation graph.
//            (activity as AppCompatActivity).setSupportActionBar(it)
//            val navController = findNavController()
//            val appBarConfiguration = AppBarConfiguration(navController.graph)
////            it.setupWithNavController(navController, appBarConfiguration)
//
//            (activity as AppCompatActivity).setupActionBarWithNavController(navController, appBarConfiguration)
////NavigationUI.setupActionBarWithNavController(activity as AppCompatActivity, navController, appBarConfiguration)
////            NavigationUI.setupActionBarWithNavController(activity as AppCompatActivity, navController)
//
//            // Set the support action bar.
//
//        }

    }

}