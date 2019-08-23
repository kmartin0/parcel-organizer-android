package com.km.parceltracker.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.km.parceltracker.R

abstract class BaseMVVMFragment<T : ViewDataBinding, V : BaseViewModel> : BaseFragment() {

    protected lateinit var binding: T
    protected lateinit var viewModel: V

    /**
     * Setup the data binding and view model connection
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        viewModel =
            if (isSharedViewModel()) ViewModelProviders.of(activity as AppCompatActivity).get(getVMClass())
            else ViewModelProviders.of(this).get(getVMClass())
        binding.lifecycleOwner = activity as AppCompatActivity
        initViewModelBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoading()
        observeNoInternetConnection()
        observeLogout()
        observeInternalServerError()
    }

    private fun observeLoading() {
        viewModel.isLoading.observe(this, Observer {
            showLoading(it)
        })
    }

    private fun observeNoInternetConnection() {
        viewModel.noInternetConnection.observe(this, Observer {
            AlertDialog.Builder(context!!)
                .setTitle(getString(R.string.error_internet_title))
                .setMessage(getString(R.string.error_internet_message))
                .setNeutralButton(getString(R.string.ok), null)
                .show()
        })
    }

    private fun observeLogout() {
        // TODO: Logout functionality
        viewModel.logout.observe(this, Observer {
            Toast.makeText(context, "Logout called!", Toast.LENGTH_LONG).show()
        })
    }

    private fun observeInternalServerError() {
        viewModel.internalServerError.observe(this, Observer {
            AlertDialog.Builder(context!!)
                .setTitle(getString(R.string.error_internal_title))
                .setMessage(getString(R.string.error_internal_message))
                .setNeutralButton(getString(R.string.ok), null)
                .show()
        })
    }

    abstract fun initViewModelBinding()

    abstract fun getVMClass(): Class<V>

    open fun isSharedViewModel(): Boolean = false

}
