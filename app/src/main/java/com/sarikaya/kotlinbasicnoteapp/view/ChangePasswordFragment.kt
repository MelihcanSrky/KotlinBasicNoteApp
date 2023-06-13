package com.sarikaya.kotlinbasicnoteapp.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.sarikaya.kotlinbasicnoteapp.R
import com.sarikaya.kotlinbasicnoteapp.databinding.FragmentChangePasswordBinding
import com.sarikaya.kotlinbasicnoteapp.viewmodel.ChangePasswordFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {

    override fun getViewBinding() = FragmentChangePasswordBinding.inflate(layoutInflater)
    val viewModel: ChangePasswordFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        changePasswordButtonClickListener()
    }

    private fun changePasswordButtonClickListener() {
        binding.saveBtn.setOnClickListener {
            val password: String = binding.passwordEditText.text.toString()
            val newPassword: String = binding.newPasswordEditText.text.toString()
            val newPasswordConfirm: String = binding.confirmNewPasswordEditText.text.toString()
            if(password.isNotEmpty() && newPassword.isNotEmpty() && newPasswordConfirm.isNotEmpty()) {
                if (newPassword == newPasswordConfirm) {
                    viewModel.changePassword(password, newPassword, newPasswordConfirm)
                } else {
                    Toast.makeText(context, "New password don't match.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeLiveData() {
        viewModel.changePasswordLiveData.observe(viewLifecycleOwner) { passwordLiveData ->
            if (passwordLiveData != null) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_changePasswordFragment_to_profilePageFragment)
            }
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
