package ru.netology.nmedia //
// класс в разработке


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.Helper
import ru.netology.nmedia.viewmodel.PostViewModel


class PostFragment() : Fragment() {

    companion object {
        var Bundle.postId by Helper.LongArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(
            inflater,
            container,
            false
        )

        val postViewHolder = PostViewHolder(binding.post, object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edited.observe(viewLifecycleOwner) { post ->//toString()
                    //newPostLauncher.launch(post.content)
                    binding.edit.setText(post.content)
                    findNavController().navigate(
                        R.id.action_postfragment_to_newPostFragmentEdit,
                        bundleOf("content" to post.content)
                    )
                    Log.d("PostFragment", "edit")
                }
            }


            override fun playVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                startActivity(intent)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onView(post: Post) {
                viewModel.viewById(post.id)
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

                /*val shareIntent =
                    Intent.createChooser(intent.getString(R.string.chooser_share_post))
                startActivity(shareIntent)
*/
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }
        })


        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == requireArguments().postId } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            postViewHolder.bind(post)
        }

        return binding.root
    }
}

/*class PostFragmentHolder(
    private val binding: FragmentPostBinding,
    private val OnInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {

            author.text = post.author
            published.text = post.published
            content.text = post.content
            content.findNavController().navigate(R.id.action_feedFragment_to_postFragment)
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

            if (post.videoUrl.isNotBlank()) {
                videoLayout.visibility = View.VISIBLE
            } else {
                videoLayout.visibility = View.GONE
            }
            playButton.setOnClickListener {
                OnInteractionListener.playVideo(post)
            }
        }
    }
}*/

/*class PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}*/