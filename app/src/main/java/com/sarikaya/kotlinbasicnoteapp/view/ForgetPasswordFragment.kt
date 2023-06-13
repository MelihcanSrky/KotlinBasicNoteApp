package com.sarikaya.kotlinbasicnoteapp.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.sarikaya.kotlinbasicnoteapp.R
import com.sarikaya.kotlinbasicnoteapp.databinding.FragmentForgetPasswordBinding
import com.sarikaya.kotlinbasicnoteapp.viewmodel.ForgotPasswordFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>() {

    override fun getViewBinding() = FragmentForgetPasswordBinding.inflate(layoutInflater)
    val viewModel: ForgotPasswordFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        collectFlow()
        editTextFocusListener()
        resetPasswordClickListener()
    }

    private fun resetPasswordClickListener() {
        binding.resPasswordBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            if (email.isNotEmpty()) {
                viewModel.forgotPassword(email)
            }
        }
    }

    private fun observeLiveData() {
        viewModel.forgotPasswordLiveData.observe(viewLifecycleOwner) { passwordData ->
            if (passwordData != null) {
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_forgetPasswordFragment_to_registerPageFragment
                )
            }
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorData ->
            if (errorData.message != null) {
                Toast.makeText(context, errorData.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun editTextFocusListener() {
        binding.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            } else {
                binding.emailEditText.addTextChangedListener {
                    validateEmail()
                }
            }
        }
    }

    private fun validateEmail() {
        val email = binding.emailEditText.text.toString()
        if (viewModel.setEmail(email) == false) {
            binding.emailErrorLayout.visibility = View.VISIBLE
            if (email.isNotEmpty()) {
                binding.emailErrorText.setText(getString(R.string.email_is_invalid))
            } else {
                binding.emailErrorText.setText(getString(R.string.email_is_required))
            }
            binding.emailETLayout.setBackgroundResource(R.drawable.input)
            binding.resPasswordBtn.isEnabled = false
        } else {
            binding.emailErrorLayout.visibility = View.GONE
            binding.emailETLayout.setBackgroundResource(R.drawable.custom_input)
            binding.resPasswordBtn.isEnabled = true
        }
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            binding.resPasswordBtn.isEnabled = viewModel.setEmail(binding.emailEditText.text.toString())
        }
    }
}
