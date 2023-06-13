package com.sarikaya.kotlinbasicnoteapp.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.sarikaya.kotlinbasicnoteapp.R
import com.sarikaya.kotlinbasicnoteapp.databinding.FragmentRegisterPageBinding
import com.sarikaya.kotlinbasicnoteapp.storage.SharedPrefManager
import com.sarikaya.kotlinbasicnoteapp.viewmodel.RegisterPageFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterPageFragment : BaseFragment<FragmentRegisterPageBinding>() {

    override fun getViewBinding() = FragmentRegisterPageBinding.inflate(layoutInflater)
    val viewModel: RegisterPageFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obseverLiveData()
        navSignIn()
        editTextFocusListener()
        collectFlow()
        signUpFun()
    }

    private fun navSignIn() {
        binding.signInNowText.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(
                R.id.action_registerPageFragment_to_loginPageFragment
            )
        }
        binding.forgotPasswordText.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(
                R.id.action_registerPageFragment_to_forgetPasswordFragment
            )
        }
    }

    private fun editTextFocusListener() {
        binding.fullNameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateFullName()
            } else {
                binding.fullNameEditText.addTextChangedListener {
                    validateFullName()
                }
            }
        }
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

    private fun signUpFun() {
        binding.signUpBtn.setOnClickListener {
            val fullName = binding.fullNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.createUser(fullName, email, password)
            }
        }
    }

    private fun obseverLiveData() {
        viewModel.loginTokenData.observe(viewLifecycleOwner) { tokenData ->
            SharedPrefManager.getInstance(requireContext()).saveUser(tokenData)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorData ->
            Toast.makeText(context, errorData.message, Toast.LENGTH_LONG).show()
        }

        viewModel.isSuccessLoading.observe(viewLifecycleOwner) { isSuccess ->
            when (isSuccess) {
                true -> {
                    Navigation.findNavController(requireView()).navigate(
                        R.id.action_registerPageFragment_to_notesPageFragment
                    )
                }
            }
        }
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.isSubmitEnabled.collect { isSubmit ->
                binding.signUpBtn.isEnabled = isSubmit
            }
        }
    }

    private fun validateFullName() {
        val fullName = binding.fullNameEditText.text.toString()
        if (viewModel.setFullName(fullName) == false) {
            binding.nameErrorLayout.visibility = View.VISIBLE
            binding.nameErrorText.setText(getString(R.string.fullname_is_required))
            binding.fullNameETLayout.setBackgroundResource(R.drawable.input)
        } else {
            binding.nameErrorLayout.visibility = View.GONE
            binding.fullNameETLayout.setBackgroundResource(R.drawable.custom_input)
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
