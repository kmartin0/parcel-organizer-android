package com.km.parcelorganizer.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
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

    private var navigationBarInset = 0
    private var navHostBaseBottomPadding = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        initViewBinding()
        setupWindowInsets()
        initViewModel()
        initNavFragment()
        setupBottomNavigationView()
    }

    private fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostBaseBottomPadding = binding.navHostFragment.paddingBottom
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val statusBars = insets.getInsets(
                WindowInsetsCompat.Type.statusBars()
            )

            navigationBarInset = insets.getInsets(
                WindowInsetsCompat.Type.navigationBars()
            ).bottom

            binding.statusBarBackground.updateLayoutParams {
                height = statusBars.top
            }

            applyNavigationBarInset()

            insets
        }

        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false

        ViewCompat.requestApplyInsets(binding.root)
    }

    private fun applyNavigationBarInset() {
        binding.navHostFragment.updatePadding(
            bottom = navHostBaseBottomPadding +
                    if (binding.bnvMain.isGone) {
                        navigationBarInset
                    } else {
                        0
                    }
        )
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.getDarkThemeObserver().observe(this) {
            delegate.localNightMode =
                if (it) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
        }
    }

    private fun setupBottomNavigationView() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bnvMain.visibility = when (destination.id) {
                R.id.parcelsFragment,
                R.id.userProfileFragment -> View.VISIBLE

                else -> View.GONE
            }

            applyNavigationBarInset()
        }

        NavigationUI.setupWithNavController(
            binding.bnvMain,
            navController
        )
    }

    /**
     * Initialize the nav host fragment. Starting destination is [LoginFragment].
     * If the app was opened using a send intent then add the plain text,
     * meant for sharing a tracking url as args.
     */
    private fun initNavFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.navHostFragment
            ) as NavHostFragment

        navController = navHostFragment.navController

        var trackingUrl: String? = null

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    trackingUrl =
                        intent.getStringExtra(Intent.EXTRA_TEXT)
                }
            }
        }

        navController.setGraph(
            R.navigation.navigation_graph,
            LoginFragmentArgs(trackingUrl).toBundle()
        )

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                R.id.loginFragment ->
                    controller.graph.setStartDestination(
                        R.id.loginFragment
                    )

                R.id.parcelsFragment ->
                    controller.graph.setStartDestination(
                        R.id.parcelsFragment
                    )
            }
        }
    }

    fun showLoading(visibility: Boolean) {
        findViewById<ProgressBar>(R.id.progressBar)?.visibility =
            if (visibility) View.VISIBLE else View.GONE
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        initNavFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) ||
                super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() ||
                super.onSupportNavigateUp()
    }
}