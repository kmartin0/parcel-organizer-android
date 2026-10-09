package nl.kmartin.parcelorganizer.ui.register

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import nl.kmartin.parcelorganizer.R
import nl.kmartin.parcelorganizer.base.BaseMVVMFragment
import nl.kmartin.parcelorganizer.databinding.FragmentRegisterBinding
import nl.kmartin.parcelorganizer.util.playAnimation

class RegisterFragment : BaseMVVMFragment<FragmentRegisterBinding, RegisterViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.registerSuccess.observe(viewLifecycleOwner, {
            onRegisterSuccess()
        })
        viewModel.alreadyExists.observe(viewLifecycleOwner, {
            binding.registerFormLayout.tilEmail.error = getString(R.string.already_exists)
        })
    }

    private fun onRegisterSuccess() {
        binding.btnRegister.isClickable = false
        binding.lottieSuccess.playAnimation {
            findNavController().navigateUp()
        }
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun getToolbar(): MaterialToolbar = binding.toolbarLayout.defaultToolbar

}