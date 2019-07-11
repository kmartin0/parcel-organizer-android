package com.km.parceltracker.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(getLayoutId(), container, false)
    }

    /**
     * Calls [showLoading] from [BaseActivity]
     */
    fun showLoading(visibility: Boolean) {
        if (activity is BaseActivity) (activity as BaseActivity).showLoading(visibility)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int
}
