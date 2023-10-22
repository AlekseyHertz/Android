package ru.netology.nmedia //

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

        val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
        )

        val postId = arguments?.getLong("postId")

        val adapter = PostViewHolder(binding.post, object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_postfragment_to_newPostFragment,
                    bundleOf("content" to post.content)
                )
                Log.d("PostFragment", "edit")
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

            /*override fun onShare(post: Post) {
                viewModel.shareById(post.id)
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

             */
        })
//        viewModel.data.observe(viewLifecycleOwner) { posts ->
//            val post = posts.find { it.id == requireArguments().postId } ?: run {
//                findNavController().navigateUp()
//                return@observe
//            }
//            postViewHolder.bind(post)
//        }

        return binding.root
    }
}