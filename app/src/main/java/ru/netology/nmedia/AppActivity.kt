package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.netology.nmedia.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity(R.layout.activity_app) {

    val binding by lazy { ActivityAppBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*supportFragmentManager.commit {
            add(R.id.nav_host_fragment, FeedFragment())
        }*/

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }
            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.nav_host_fragment).navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply {
                    //textArg = text
                }
            )
        }
    }
}