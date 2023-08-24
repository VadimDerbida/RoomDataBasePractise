package com.example.fragment_homework.profile_feature.presentation

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fragment_homework.core.domain.models.User
import com.example.fragment_homework.databinding.FragmentProfileBinding
import com.example.fragment_homework.profile_feature.domain.ProfileUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFlow()

        binding.logOutButton.setOnClickListener{
            viewModel.logOut()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignUpFragment())
        }
    }


    private fun setupUi(user: User) {
       val string = SpannableStringBuilder()
         .append("Your email is ")
          .bold { append(user.email+"\n")}
          .append("Your name is ")
         .bold { append(user.name+"\n")}
         .append("Your password is ")
          .bold {
              user.password.forEach {
                  append("*")
              }
          }

      binding.contentText.text = string
   }

    private fun observeFlow() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collectLatest { state ->

                when (state.user) {
                    null -> Unit
                    else -> setupUi(state.user)
                }

                state.events.firstOrNull()?.let { event ->
                    when (event) {
                        is ProfileUiState.Events.NavigationToSignUp -> findNavController().navigate(
                            ProfileFragmentDirections.actionProfileFragmentToSignUpFragment()) }
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


