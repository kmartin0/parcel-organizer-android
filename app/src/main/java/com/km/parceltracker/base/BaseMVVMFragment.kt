package com.km.parceltracker.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

abstract class BaseMVVMFragment<T : ViewDataBinding, V : ViewModel> : BaseFragment() {

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

    abstract fun initViewModelBinding()

    abstract fun getVMClass(): Class<V>

    open fun isSharedViewModel(): Boolean = false

}
