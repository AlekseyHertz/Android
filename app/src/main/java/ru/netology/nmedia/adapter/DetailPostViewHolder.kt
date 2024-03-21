package ru.netology.nmedia.adapter

import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentDetailPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.convertCount
import ru.netology.nmedia.util.AndroidUtil.glideDownloadAttachUrl

class DetailPostViewHolder (
    private val binding: FragmentDetailPostBinding,
    private val onInteractionListener: OnInteractionListener,
): RecyclerView.ViewHolder(binding.root){
    private val serverPathUrl = "${BuildConfig.BASE_URL}"
    private val avatarsPathUrl = "${serverPathUrl}/avatars"
    private val attachmentsUrl = "${serverPathUrl}/media"

    fun bind(post: Post) {
        binding.apply {
            val downloadAvatarUrl = "${avatarsPathUrl}/${post.authorAvatar}"
            if (post.attachment != null) {
                val downloadAttachUrl = "${attachmentsUrl}/${post.attachment!!.url}"
                glideDownloadAttachUrl(downloadAttachUrl, binding.attachment)
                binding.attachment.visibility = View.VISIBLE
            } else {
                binding.attachment.visibility = View.GONE
            }
            glideDownloadAttachUrl(downloadAvatarUrl, binding.avatar)
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.text = convertCount(post.likes)
            like.isChecked = post.likedByMe
            menu.isVisible = post.ownerByMe
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

            like.setOnClickListener {
                like.isChecked = !like.isChecked
                onInteractionListener.onLike(post)
            }

            attachment.setOnClickListener {
                onInteractionListener.onImage(post)
            }

            root.setOnClickListener {
                onInteractionListener.onPost(post)
            }
        }
    }
}