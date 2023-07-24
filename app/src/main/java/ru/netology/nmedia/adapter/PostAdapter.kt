package ru.netology.nmedia.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
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
}

class PostAdapter(
    private val OnInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding,
            OnInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val OnInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

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
                Log.d("stuff", "like") // оставим для logcat
                OnInteractionListener.onLike(post)
                //likeCallBack(post)
            }

            share.isChecked = post.shareByMe
            share.setOnClickListener {
                Log.d("stuff", "share") // оставим для logcat
                OnInteractionListener.onShare(post)
                //shareCallBack(post)
            }

            views.setOnClickListener {
                Log.d("stuff", "view") // оставим для logcat
                OnInteractionListener.onView(post)
                //viewCallBack(post)
            }


            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                OnInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                OnInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            if (post.videoUrl != null) {
                videoLayout.visibility = View.VISIBLE

                videoView.apply {
                    setVideoURI(Uri.parse(post.videoUrl))
                    requestFocus()
                    start()
                }
            } else {
                videoLayout.visibility = View.GONE
            }
            videoLayout.setOnClickListener {
                OnInteractionListener.playVideo(post)
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