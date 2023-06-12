package ru.netology.nmedia

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.convertCount
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likeCount?.text = convertCount(post.likes)
                shareCount?.text = convertCount(post.sharedCount)
                viewsCount?.text = convertCount(post.viewsCount)

                like?.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
            }
        }
        with(binding) {
            root.setOnClickListener {
                Log.d("stuff", "stuff")
                println("сработал root обработчик")
            }

            avatar.setOnClickListener {
                Log.d("stuff", "avatar")
                println("сработал avatar обработчик")
            }

            views?.setOnClickListener {
                Log.d("stuff", "views")
                println("сработал view обработчик")
                viewModel.view()
            }

            share?.setOnClickListener {
                Log.d("stuff", "share")
                println("сработал share обработчик")
                viewModel.share()
            }

            like?.setOnClickListener {
                Log.d("stuff", "like")
                println("сработал like обработчик")
                viewModel.like()
            }
        }
    }
}