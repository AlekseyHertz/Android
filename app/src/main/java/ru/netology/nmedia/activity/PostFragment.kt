package ru.netology.nmedia.activity //

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.DetailPostViewHolder
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.databinding.FragmentDetailPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtil
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment() : Fragment() {
    companion object {
        var Bundle.postId by AndroidUtil.LongArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailPostBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: PostViewModel by activityViewModels()
        val postId = arguments?.getLong("postId")

        val adapter = DetailPostViewHolder (binding, object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_postfragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }


            /*override fun playVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                startActivity(intent)
            }*/

            override fun onLike(post: Post) {
                viewModel.likeById(post)
            }

            /*override fun onView(post: Post) {
                viewModel.viewById(post.id)
            }*/

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
                findNavController().navigateUp()
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val shareIntent = Intent.createChooser(intent, "Share post")
                startActivity(shareIntent)

                /*val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }

             */
            }
//        viewModel.data.observe(viewLifecycleOwner) { posts ->
//            val post = posts.find { it.id == requireArguments().postId } ?: run {
//                findNavController().navigateUp()
//                return@observe
//            }
//            postViewHolder.bind(post)
//        }
        })
        return binding.root
    }
}