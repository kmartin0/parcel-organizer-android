package com.km.parcelorganizer.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            if (isSharedViewModel()) ViewModelProvider(activity as AppCompatActivity).get(
                getVMClass()
            )
            else ViewModelProvider(this).get(getVMClass())
        binding.lifecycleOwner = activity as AppCompatActivity
        initViewModelBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoading()
        observeServiceUnavailableException()
        observeLogout()
        observeInternalServerError()
    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    private fun observeServiceUnavailableException() {
        viewModel.serverUnavailableException.observe(this, {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.error_service_unavailable_title))
                .setIcon(R.drawable.ic_error_24dp)
                .setMessage(getString(R.string.error_service_unavailable_message))
                .setNeutralButton(getString(R.string.ok), null)
                .show()
        })
    }

    private fun observeLogout() {
        viewModel.logout.observe(this, {
            MaterialAlertDialogBuilder(requireContext())
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
            NavOptions.Builder()
                .setPopUpTo(R.id.navigation_graph, true)
                .setPopEnterAnim(android.R.anim.slide_in_left)
                .setPopExitAnim(android.R.anim.slide_out_right)
                .setEnterAnim(android.R.anim.slide_in_left)
                .setExitAnim(android.R.anim.slide_out_right)
                .build()
        )
    }

    private fun observeInternalServerError() {
        viewModel.internalServerError.observe(this, {
            MaterialAlertDialogBuilder(requireContext())
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
