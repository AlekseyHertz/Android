package ru.netology.nmedia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

class MainActivity : AppCompatActivity() {

    var View.isVisible: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            visibility = if (value) View.VISIBLE else View.GONE
        }

    val viewModel: PostViewModel by viewModels()

    val newPostLauncher = registerForActivityResult(NewPostContract) { text ->
        text?: return@registerForActivityResult
        viewModel.changeContent(text.toString())
        viewModel.save()
    }

    val interaction: OnInteractionListener = object : OnInteractionListener {
        override fun onEdit(post: Post) {
            viewModel.edit(post)
            newPostLauncher.launch(post.content)
        }

        override fun playVideo (post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                startActivity(intent)
        }

        override fun onLike(post: Post) {
            viewModel.likeById(post.id)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post.id)
        }

        override fun onShare(post: Post) {
            //viewModel.shareById(post.id)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.content)
            }

            val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(chooser)
        }

        override fun onView(post: Post) {
            viewModel.viewById(post.id)
        }

        override fun abortText(post: Post) {
            viewModel.abortText()
        }

        override fun save(post: Post) {
            viewModel.save()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(interaction)
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.add.setOnClickListener {
            newPostLauncher.launch(null)
        }

        /*viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                requestFocus()
                setText(post.content)
            }
        }

        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Content can't be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }

        binding.abortText.setOnClickListener {
            binding.content.setText("")
            viewModel.abortText()
        }

        binding.content.doOnTextChanged { _, _, _, count ->
            binding.abortText.isVisible = count != 0
        }*/
    }
}