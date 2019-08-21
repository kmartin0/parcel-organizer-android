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
                "    \"access_token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjY0NDY0NTcsInVzZXJfbmFtZSI6ImtldiIsImp0aSI6IjY3ZGFjN2I5LTliNDMtNGJiMC1iNWZiLTc2Yjc0NzM1YTM4YiIsImNsaWVudF9pZCI6InBhcmNlbC10cmFja2VyLWFuZHJvaWQiLCJzY29wZSI6WyJhbGwiXX0.LZooikEnnKBmGWnTRwbnJ0EfGikO5BhpsIEkzWg_-p0\",\n" +
                "    \"token_type\": \"bearer\",\n" +
                "    \"refresh_token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjY2NTI3NTcsInVzZXJfbmFtZSI6ImtldiIsImp0aSI6IjA0NDdlODk1LWIwYzktNDk2MC1iNzNlLWNkYjA1MjgxYTY0YiIsImNsaWVudF9pZCI6InBhcmNlbC10cmFja2VyLWFuZHJvaWQiLCJzY29wZSI6WyJhbGwiXSwiYXRpIjoiNjdkYWM3YjktOWI0My00YmIwLWI1ZmItNzZiNzQ3MzVhMzhiIn0.rkeuD9F04J1Fd_3dtDGf-HJCn5gOHzhwFTiQNXbxjHk\",\n" +
                "    \"expires_in\": 49999,\n" +
                "    \"scope\": \"all\",\n" +
                "    \"jti\": \"67dac7b9-9b43-4bb0-b5fb-76b74735a38b\"\n" +
                "}", Authorization::class.java)

                val userRepository = UserRepository(this)
        val user = User(
            4L,
            "kevin.martin@live.nl",
            "Kevin",
            "pass123",
            authorization
        )
        userRepository.persistUser(user)
    }
}
