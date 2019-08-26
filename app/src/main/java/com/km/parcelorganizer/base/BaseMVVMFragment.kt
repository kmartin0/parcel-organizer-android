package com.km.parcelorganizer.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.km.parcelorganizer.R

abstract class BaseMVVMFragment<T : ViewDataBinding, V : BaseViewModel> : BaseFragment() {

    protected lateinit var binding: T
    protected lateinit var viewModel: V

    /**
     * Setup the data binding and view model connection
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        viewModel =
            if (isSharedViewModel()) ViewModelProviders.of(activity as AppCompatActivity).get(
                getVMClass()
            )
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
                .setIcon(R.drawable.ic_error_24dp)
                .setMessage(getString(R.string.error_internet_message))
                .setNeutralButton(getString(R.string.ok), null)
                .show()
        })
    }

    private fun observeLogout() {
        viewModel.logout.observe(this, Observer {
            AlertDialog.Builder(context!!)
                .setTitle(getString(R.string.error_authentication_title))
                .setIcon(R.drawable.ic_error_24dp)
                .setMessage(getString(R.string.error_authentication_message))
                .setNeutralButton(getString(R.string.ok)) { _, _ -> logout() }
                .setCancelable(false)
                .show()
        })
    }

    protected fun logout() {
        viewModel.logout()
        findNavController().navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.navigation_graph, true).build()
        )
    }

    private fun observeInternalServerError() {
        viewModel.internalServerError.observe(this, Observer {
            AlertDialog.Builder(context!!)
                .setTitle(getString(R.string.error_internal_title))
                .setIcon(R.drawable.ic_error_24dp)
                .setMessage(getString(R.string.error_internal_message))
                .setNeutralButton(getString(R.string.ok), null)
                .show()
        })
    }

    abstract fun initViewModelBinding()

    abstract fun getVMClass(): Class<V>

    open fun isSharedViewModel(): Boolean = false

}
