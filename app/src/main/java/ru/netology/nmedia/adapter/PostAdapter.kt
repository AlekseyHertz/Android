package ru.netology.nmedia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.convertCount

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onView(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun save(post: Post) {}
    fun abortText(post: Post) {}
    fun playVideo(post: Post) {}
    fun onPost(post: Post) {}
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding,
            onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published.toString()
            content.text = post.content
            content.setOnClickListener {
                Log.d("stuff", "content")
                onInteractionListener.onPost(post)

            }
            like.text = convertCount(post.likes)
            share.text = convertCount(post.sharedCount)
            //likeCount.text = convertCount(post.likes)
            //shareCount.text = convertCount(post.sharedCount)
            viewsCount.text = convertCount(post.viewsCount)
            like.isChecked = post.likedByMe
            /*like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )*/

            like.setOnClickListener {
                like.isCheckable = !like.isCheckable
                Log.d("stuff", "like") // оставим для logcat
                onInteractionListener.onLike(post)
                //likeCallBack(post)
            }

            share.isChecked = post.shareByMe
            share.setOnClickListener {
                Log.d("stuff", "share") // оставим для logcat
                onInteractionListener.onShare(post)
                //shareCallBack(post)
            }

            views.setOnClickListener {
                Log.d("stuff", "view") // оставим для logcat
                onInteractionListener.onView(post)
                //viewCallBack(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            if (!post.videoUrl.isNullOrBlank()) {
                videoLayout.visibility = View.VISIBLE
            } else {
                videoLayout.visibility = View.GONE
            }
            playButton.setOnClickListener {
                onInteractionListener.playVideo(post)
            }

            root.setOnClickListener {
                onInteractionListener.onPost(post)
                Log.d("post", "post")
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
