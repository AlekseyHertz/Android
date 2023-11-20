package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentRegistrationBinding
import ru.netology.nmedia.util.AndroidUtil
import ru.netology.nmedia.vi.AuthViewModel
import ru.netology.nmedia.viewmodel.RegistrationViewModel

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val authViewModel: AuthViewModel by activityViewModels()
        val registrationViewModel: RegistrationViewModel by activityViewModels()


        val binding = FragmentRegistrationBinding.inflate(
            inflater,
            container,
            false
        )

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        authViewModel.data.observe(viewLifecycleOwner) {
            binding.register.setOnClickListener {
                AndroidUtil.AndroidUtils.hideKeyboard(requireView())

                val userLogin = binding.userLogin.text.toString()
                val userPassword = binding.userPassword.text.toString()
                val userName = binding.userName.text.toString()

                if (userName.isBlank() || userLogin.isBlank() || userPassword.isBlank()) {
                    Snackbar.make(binding.root, R.string.error_empty_content, Snackbar.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }

                registrationViewModel.tryRegistration(
                    login = userLogin,
                    password = userPassword,
                    username = userName
                )
            }
            if (authViewModel.authorized) {
                findNavController().navigate(R.id.feedFragment)
            }
        }



        return binding.root
    }
}