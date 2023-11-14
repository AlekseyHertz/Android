package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentAuthBinding
import ru.netology.nmedia.util.AndroidUtil
import ru.netology.nmedia.vi.AuthViewModel
import ru.netology.nmedia.viewmodel.LoginViewModel

class FragmentAuth : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val authViewModel by viewModels<AuthViewModel>()
        val binding = FragmentAuthBinding.inflate(
            inflater,
            container,
            false
        )
        val loginViewModel: LoginViewModel by activityViewModels()

        binding.sighInAuth.setOnClickListener {
            val accountName = binding.username.toString()
            val accountPassword = binding.password.text.toString()

            if (accountName.isBlank() || accountPassword.isBlank()) {
                AndroidUtil.AndroidUtils.hideKeyboard(requireView())
                Snackbar.make(binding.root, R.string.noLogin, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            loginViewModel.signLogin(username = accountName, password = accountPassword)
            authViewModel.data.observe(viewLifecycleOwner) {
                if (authViewModel.authorized) {
                    findNavController().navigateUp()
                }
            }
        }

        return binding.root
    }

}