package com.km.parceltracker.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.google.gson.Gson
import com.km.parceltracker.R
import com.km.parceltracker.model.Authorization
import com.km.parceltracker.model.User
import com.km.parceltracker.repository.UserRepository
import com.km.parceltracker.ui.parcels.ParcelsFragmentDirections

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginTestUser()
        checkAppShared()
    }

    private fun checkAppShared() {
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    val action = ParcelsFragmentDirections.actionParcelsFragmentToCreateParcelFragment(
                        intent.getStringExtra(Intent.EXTRA_TEXT)
                    )
                    findNavController(R.id.navHostFragment).navigate(action)
                }
            }
        }
    }

    fun showLoading(visibility: Boolean) {
        findViewById<ProgressBar>(R.id.progressBar)?.visibility = if (visibility) View.VISIBLE else View.GONE
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

    private fun loginTestUser() {
        val authorization= Gson().fromJson("{\n" +
                "    \"access_token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjYzNzk5NjksInVzZXJfbmFtZSI6ImtldiIsImp0aSI6ImUzOWJlZjk3LWY1MzEtNDUxMy1hOTAxLTgzOTg1NjI2ODY3NyIsImNsaWVudF9pZCI6InBhcmNlbC10cmFja2VyLWFuZHJvaWQiLCJzY29wZSI6WyJhbGwiXX0.WWm5JEPQdtUMjXvXnm79k_vtiHLliyK1C4_XCwWEOUU\",\n" +
                "    \"token_type\": \"bearer\",\n" +
                "    \"refresh_token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjYzNzk5NjksInVzZXJfbmFtZSI6ImtldiIsImp0aSI6Ijk1NTU0MzM0LTNiMjAtNDY1My1hM2E0LTRmZmU0ZmU4ZGMzMyIsImNsaWVudF9pZCI6InBhcmNlbC10cmFja2VyLWFuZHJvaWQiLCJzY29wZSI6WyJhbGwiXSwiYXRpIjoiZTM5YmVmOTctZjUzMS00NTEzLWE5MDEtODM5ODU2MjY4Njc3In0.1pZOeqrV_Gc4PhlAZTwQFj5mqy7ExVGMnyat-8sZkcA\",\n" +
                "    \"expires_in\": 4,\n" +
                "    \"scope\": \"all\",\n" +
                "    \"jti\": \"e39bef97-f531-4513-a901-839856268677\"\n" +
                "}", Authorization::class.java)

                val userRepository = UserRepository(this)
        val user = User(
            4L,
            "kevin.martin@live.nl",
            "Kevin",
            "pass123",
            authorization
        )
        userRepository.loginUser(user)
    }
}
