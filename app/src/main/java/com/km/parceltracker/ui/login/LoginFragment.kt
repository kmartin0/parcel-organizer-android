package com.km.parceltracker.ui.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.km.parceltracker.R
import com.km.parceltracker.base.BaseMVVMFragment
import com.km.parceltracker.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseMVVMFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        // When login is successful then navigate to the ParcelsFragment.
        viewModel.loginSuccess.observe(this, Observer {
            findNavController().navigate(R.id.action_loginFragment_to_parcelsFragment)
        })
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_login

}