package com.sarikaya.kotlinbasicnoteapp.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.sarikaya.kotlinbasicnoteapp.R
import com.sarikaya.kotlinbasicnoteapp.databinding.FragmentLoginPageBinding
import com.sarikaya.kotlinbasicnoteapp.storage.SharedPrefManager
import com.sarikaya.kotlinbasicnoteapp.viewmodel.LoginPageFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginPageFragment : BaseFragment<FragmentLoginPageBinding>() {

    override fun getViewBinding() = FragmentLoginPageBinding.inflate(layoutInflater)
    val viewModel: LoginPageFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        editTextFocusListener()
        collectFlow()
        signInBtn()
        navButtons()
    }

    private fun navButtons() {
        binding.signUpNowText.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(
                R.id.action_loginPageFragment_to_registerPageFragment
            )
        }
        binding.forgotPasswordText.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(
                R.id.action_loginPageFragment_to_forgetPasswordFragment
            )
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
        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            } else {
                binding.passwordEditText.addTextChangedListener {
                    validatePassword()
                }
            }
        }
    }

    private fun signInBtn() {
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.userLogin(email, password)
            }
        }
    }

    private fun observeLiveData() {
        viewModel.loginTokenData.observe(viewLifecycleOwner) { tokenData ->
            SharedPrefManager.getInstance(requireContext()).saveUser(tokenData)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorData ->
            Toast.makeText(context, errorData.message, Toast.LENGTH_LONG).show()
        }

        viewModel.isSuccessLoading.observe(viewLifecycleOwner) { bool ->
            when (bool) {
                true -> {
                    Navigation.findNavController(requireView()).navigate(
                        R.id.action_loginPageFragment_to_notesPageFragment
                    )
                }
            }
        }
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.isSubmitEnabled.collect { isSubmit ->
                binding.loginBtn.isEnabled = isSubmit
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
        } else {
            binding.emailErrorLayout.visibility = View.GONE
            binding.emailETLayout.setBackgroundResource(R.drawable.custom_input)
        }
    }

    private fun validatePassword() {
        val password = binding.passwordEditText.text.toString()
        if (viewModel.setPassword(password) == false) {
            binding.passwordErrorLayout.visibility = View.VISIBLE
            if (password.isNotEmpty()) {
                binding.passwordErrorText.setText(getString(R.string.password_is_invalid))
            } else {
                binding.passwordErrorText.setText(getString(R.string.password_is_required))
            }
            binding.passwordETLayout.setBackgroundResource(R.drawable.input)
        } else {
            binding.passwordErrorLayout.visibility = View.GONE
            binding.passwordETLayout.setBackgroundResource(R.drawable.custom_input)
        }
    }
}
