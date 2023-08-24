package com.example.fragment_homework.auth_feature.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fragment_homework.auth_feature.domain.state.LogInUiState
import com.example.fragment_homework.auth_feature.domain.state.SignUpUiState
import com.example.fragment_homework.databinding.FragmentLogInBinding
import com.example.fragment_homework.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint

class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LogInViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFlow()

        binding.createNewButton.setOnClickListener {
            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
        }

        binding.loginButton.setOnClickListener {
            viewModel.logIn(
                email = binding.emailTextInput.text.toString(),
                name = binding.nameTextInput.text.toString(),
                password = binding.passwordTextInput.text.toString()
            )
        }
    }

    private fun observeFlow() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collectLatest { state ->
                when (state.loading) {
                    true -> binding.loadingIndicator.visibility = View.VISIBLE
                    false -> binding.loadingIndicator.visibility = View.GONE
                }

                state.events.firstOrNull()?.let { event ->
                    when (event) {
                        is LogInUiState.Events.NavigationToProfile -> findNavController().navigate(
                            LogInFragmentDirections.actionLogInFragmentToProfileFragment()
                        )
                        is LogInUiState.Events.ShowError -> Snackbar.make(
                            requireView(),
                            event.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    viewModel.consumeEvent(event)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}