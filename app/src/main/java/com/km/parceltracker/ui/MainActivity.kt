package com.km.parceltracker.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.km.parceltracker.R
import com.km.parceltracker.ui.login.LoginFragment
import com.km.parceltracker.ui.login.LoginFragmentArgs

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavFragment()
    }

    /**
     * Initialize the nav host fragment. Starting destination is [LoginFragment].
     * If the app was opened using a send intent then add the plain text, meant for sharing a tracking url as args.
     */
    private fun initNavFragment() {
        var trackingUrl: String? = null
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    trackingUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
                }
            }
        }

        findNavController(R.id.navHostFragment).setGraph(
            R.navigation.navigation_graph,
            LoginFragmentArgs(trackingUrl).toBundle()
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initNavFragment()
    }

    fun showLoading(visibility: Boolean) {
        findViewById<ProgressBar>(R.id.progressBar)?.visibility =
            if (visibility) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Have the NavigationUI look for an action or destination matching the menu
//        // item id and navigate there if found.
//        // Otherwise, bubble up to the parent.
        return item.onNavDestinationSelected(findNavController(R.id.navHostFragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.navHostFragment).navigateUp()
                || super.onSupportNavigateUp()
    }
}
