package nl.kmartin.parcelorganizer.ui.userprofile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import nl.kmartin.parcelorganizer.R
import nl.kmartin.parcelorganizer.base.BaseMVVMFragment
import nl.kmartin.parcelorganizer.databinding.FragmentUserProfileBinding

class UserProfileFragment : BaseMVVMFragment<FragmentUserProfileBinding, UserProfileViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.btnChangeProfile.setOnClickListener { navigateToUpdateProfile() }
        binding.btnChangePassword.setOnClickListener { navigateToChangePassword() }
        binding.btnLogout.setOnClickListener { showLogoutDialog() }

        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onChangeDarkTheme(
                isChecked
            )
        }
    }

    private fun navigateToUpdateProfile() {
        findNavController().navigate(R.id.action_userProfileFragment_to_updateProfileFragment)
    }

    private fun navigateToChangePassword() {
        findNavController().navigate(R.id.action_userProfileFragment_to_changePasswordFragment)
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_logout_title))
            .setMessage(getString(R.string.dialog_logout_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> logout() }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<UserProfileViewModel> = UserProfileViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_user_profile

    override fun onStart() {
        super.onStart()
        viewModel.refreshUser()
    }

}