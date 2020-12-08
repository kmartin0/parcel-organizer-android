package com.km.parcelorganizer.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.km.parcelorganizer.R
import com.km.parcelorganizer.databinding.ActivityMainBinding
import com.km.parcelorganizer.ui.login.LoginFragment
import com.km.parcelorganizer.ui.login.LoginFragmentArgs

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        initViewModel()
        initNavFragment()
        setupBottomNavigationView()
    }

    private fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.getDarkThemeObserver().observe(this, {
            delegate.localNightMode =
                if (it) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        })
    }

    private fun setupBottomNavigationView() {
        val navHost = navController
        navHost.addOnDestinationChangedListener { _, destination, _ ->
            binding.bnvMain.visibility = when (destination.id) {
                R.id.parcelsFragment, R.id.userProfileFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
        NavigationUI.setupWithNavController(binding.bnvMain, navHost)
    }

    /**
     * Initialize the nav host fragment. Starting destination is [LoginFragment].
     * If the app was opened using a send intent then add the plain text, meant for sharing a tracking url as args.
     */
    private fun initNavFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        var trackingUrl: String? = null
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    trackingUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
                }
            }
        }

        navController.setGraph(
            R.navigation.navigation_graph,
            LoginFragmentArgs(trackingUrl).toBundle()
        )

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> controller.graph.startDestination = R.id.loginFragment
                R.id.parcelsFragment -> controller.graph.startDestination = R.id.parcelsFragment
            }
        }
    }

    fun showLoading(visibility: Boolean) {
        findViewById<ProgressBar>(R.id.progressBar)?.visibility =
            if (visibility) View.VISIBLE else View.GONE
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initNavFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Have the NavigationUI look for an action or destination matching the menu
//        // item id and navigate there if found.
//        // Otherwise, bubble up to the parent.
        return item.onNavDestinationSelected(navController)
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }
}
