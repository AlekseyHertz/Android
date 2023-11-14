package ru.netology.nmedia.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.ActivityAppBinding
import ru.netology.nmedia.vi.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity(){//R.layout.activity_app) {

    @Inject
    lateinit var appAuth: AppAuth

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        val binding =
            ActivityAppBinding.inflate(layoutInflater)//by lazy { ActivityAppBinding.inflate(layoutInflater) }

        /*supportFragmentManager.commit {
            add(R.id.nav_host_fragment, FeedFragment())
        }*/
        setContentView(binding.root)
        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        finish()
                    }
                    .show()
                return@let
            }

            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.nav_host_fragment)
                .navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )
        }
        val authViewModel by viewModels<AuthViewModel>()
        var currentMenuProvider: MenuProvider? = null
        authViewModel.data.observe(this) {
            currentMenuProvider?.let(::removeMenuProvider)

            addMenuProvider(
                object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(R.menu.auth_menu, menu)
                        menu.setGroupVisible(R.id.registered, authViewModel.authorized)
                        menu.setGroupVisible(R.id.unregistered, !authViewModel.authorized)
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                        when (menuItem.itemId) {
                            R.id.login -> {
                                findNavController(R.id.nav_host_fragment)
                                    .navigate(R.id.action_feedFragment_to_fragment_auth)
                                //dependencyContainer.appAuth.setAuth(Token(5L, "x-token"))
                                true
                            }

                            R.id.logout -> {
                                appAuth.clear()
                                true
                            }

//                            R.id.registration -> {
//                                findNavController(R.id.string.registration)
//                            }

                            else -> false
                        }

                }.also {
                    currentMenuProvider = it
                }, this
            )
        }
        //checkGoogleApiAvailability()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            return
        }
        requestPermissions(arrayOf(permission), 1)
    }
}