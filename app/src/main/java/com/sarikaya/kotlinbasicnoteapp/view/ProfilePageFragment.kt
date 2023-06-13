package com.sarikaya.kotlinbasicnoteapp.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.sarikaya.kotlinbasicnoteapp.R
import com.sarikaya.kotlinbasicnoteapp.databinding.FragmentProfilePageBinding
import com.sarikaya.kotlinbasicnoteapp.storage.SharedPrefManager
import com.sarikaya.kotlinbasicnoteapp.viewmodel.ProfilePageFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfilePageFragment : BaseFragment<FragmentProfilePageBinding>() {

    override fun getViewBinding() = FragmentProfilePageBinding.inflate(layoutInflater)
    val viewModel: ProfilePageFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlow()
        observeViewLiveData()
        navigationBtns()
        saveMeBtn()
        editTextFocusListener()
    }

    private fun navigationBtns() {
        binding.signOutText.setOnClickListener {
            SharedPrefManager.getInstance(requireContext()).clear()
            Navigation.findNavController(requireView()).navInflater.inflate(R.navigation.navgraph)
                .setStartDestination(R.id.registerPageFragment)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_profilePageFragment_to_registerPageFragment)
        }
        binding.changePasswordText.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_profilePageFragment_to_changePasswordFragment)
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
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.isSubmitEnabled.collect { isSubmit ->
                binding.saveBtn.isEnabled = isSubmit
            }
        }
    }

    private fun saveMeBtn() {
        binding.saveBtn.setOnClickListener {
            val fullName = binding.fullNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            if (binding.fullNameEditText.text.isNotEmpty() && binding.emailEditText.text.isNotEmpty()) {
                viewModel.updateMe(fullName, email)
                Toast.makeText(context, "Profile Updated!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun observeViewLiveData() {
        viewModel.getMe()
        viewModel.userGetMeLiveData.observe(viewLifecycleOwner) { getMeData ->
            binding.fullNameEditText.setText(getMeData.data.fullName)
            binding.emailEditText.setText(getMeData.data.email)
            validateFullName()
            validateEmail()
        }
        viewModel.userUpdateMeLiveData.observe(viewLifecycleOwner) { updateMeData ->
            binding.fullNameEditText.setText(updateMeData.data.fullName)
            binding.emailEditText.setText(updateMeData.data.email)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorData ->
            Toast.makeText(context, errorData.message, Toast.LENGTH_LONG).show()
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

}
