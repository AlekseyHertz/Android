package ru.netology.nmedia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.convertCount

typealias LikeCallBack = (Post) -> Unit
//typealias ShareCallBack = (Post) -> Unit
//typealias ViewCallBack = (Post) -> Unit

class PostAdapter(private val likeCallBack: LikeCallBack)://, private val shareCallBack: ShareCallBack, private val viewCallBack: ViewCallBack) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, likeCallBack)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem (position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val likeCallBack: LikeCallBack,
    //private val shareCallBack: ShareCallBack,
    //private val viewCallBack: ViewCallBack
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = convertCount(post.likes)
            shareCount.text = convertCount(post.sharedCount)
            viewsCount.text = convertCount(post.viewsCount)
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )
            like.setOnClickListener {
                Log.d("stuff", "like") // оставим для logcat
                likeCallBack(post)//viewModel.likeById(post.id)
            }
            /*shareCount.setOnClickListener {
                Log.d("stuff", "share")
                shareCallBack(post)
            }
            viewsCount.setOnClickListener {
                Log.d("stuff", "view")
                viewCallBack(post)
            }*/
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}

/*with(binding) {
            root.setOnClickListener {
                Log.d("stuff", "stuff")
                println("сработал root обработчик")
            }

            avatar.setOnClickListener {
                Log.d("stuff", "avatar")
            }

            views?.setOnClickListener {
                Log.d("stuff", "views") // оставим для logcat
                viewModel.view()
            }

            share?.setOnClickListener {
                Log.d("stuff", "share") // оставим для logcat
                viewModel.share()
            }
        }*/