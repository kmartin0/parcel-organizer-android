package com.km.parceltracker.base

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.km.parceltracker.R


abstract class BaseActivity : AppCompatActivity() {

    /**
     * Initialize the layout and title
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (inflateLayout()) setContentView(getLayoutId())
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(getToolbar())
        title = getActivityTitle()
    }

    /**
     * Displays a loading indicator based on [visibility]
     */
    fun showLoading(visibility: Boolean) {
        findViewById<ProgressBar>(R.id.progressBar)?.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    /**
     * Adds a [Fragment] to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment The fragment to be added.
     */
    fun addFragment(containerViewId: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerViewId, fragment)
        fragmentTransaction.commit()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(getMenuLayoutId(), menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getActivityTitle(): String

    abstract fun getToolbar(): Toolbar?

    protected open fun inflateLayout(): Boolean = true

//    protected open fun getMenuLayoutId(): Int = R.menu.menu_main

}
