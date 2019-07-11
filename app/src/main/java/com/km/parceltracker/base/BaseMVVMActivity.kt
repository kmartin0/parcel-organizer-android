package com.km.parceltracker.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

abstract class BaseMVVMActivity<T : ViewDataBinding, V : BaseViewModel> : BaseActivity() {
    protected lateinit var binding: T
    protected lateinit var viewModel: V

    /**
     * Setup the data binding and view model
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = ViewModelProviders.of(this).get(getVMClass())
        initViewModelBinding()
    }

    /**
     * Display the loading indicator based on the [BaseViewModel.isLoading]
     */
    fun initLoadingObserver() {
        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading != null) showLoading(isLoading) else showLoading(false)
        })
    }

    abstract fun initViewModelBinding()

    abstract fun getVMClass(): Class<V>

    override fun inflateLayout(): Boolean = false

}
