package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentAuthBinding
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.AndroidUtil
import ru.netology.nmedia.vi.AuthViewModel
import ru.netology.nmedia.viewmodel.LoginViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FragmentAuth : Fragment() {

    @Inject
    lateinit var repository: PostRepositoryImpl
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val authViewModel: AuthViewModel by activityViewModels()
        val binding = FragmentAuthBinding.inflate(
            inflater,
            container,
            false
        )
        val loginViewModel: LoginViewModel by activityViewModels()

        loginViewModel.dataState.observe(viewLifecycleOwner) { state ->
            when {
                state.userNotFoundError || state.incorrectPasswordError -> Toast.makeText(
                    context,
                    R.string.noLogin,
                    Toast.LENGTH_LONG
                ).show()

                state.error -> Toast.makeText(
                    context,
                    R.string.unknow_error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        authViewModel.data.observe(viewLifecycleOwner) {
            if (authViewModel.authorized) {
                findNavController().navigateUp()
            }
            binding.login.setOnClickListener {
                AndroidUtil.AndroidUtils.hideKeyboard(requireView())
                val accountName = binding.userLogin.editText?.text.toString()
                val accountPassword = binding.password.editText?.text.toString()
                if (accountName.isBlank() || accountPassword.isBlank()) {
                    Snackbar.make(binding.root, R.string.noLogin, Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                loginViewModel.signLogin(login = accountName, password = accountPassword)


                /*loginViewModel.signLogin(username = accountName, password = accountPassword)
                    authViewModel.data.observe(viewLifecycleOwner) {
                        if (authViewModel.authorized) {
                            findNavController().navigateUp()
                        }
                    }*/
            }
        }

        binding.navRegister.setOnClickListener {
            if (authViewModel.authorized) {
                findNavController().navigate(R.id.registerFragment)
            }
        }

        return binding.root
    }
}