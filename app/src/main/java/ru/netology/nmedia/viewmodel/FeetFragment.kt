package ru.netology.nmedia.viewmodel // из MainActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.PostFragment.Companion.postId
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.NewPostFragment.Companion.textArg

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val viewModel2: PostViewModel by activityViewModels()

    /*val newPostLauncher = registerForActivityResult(NewPostContract) { text ->
        text?: return@registerForActivityResult
        viewModel.changeContent(text.toString())
        viewModel.save()
    }

    val interaction: OnInteractionListener = object : OnInteractionListener {


        override fun onView(post: Post) {
            viewModel.viewById(post.id)
        }

        override fun abortText(post: Post) {
            viewModel.abortText()
        }

        override fun save(post: Post) {
            viewModel.save()
        }
    }*/

    val adapter = PostAdapter(object : OnInteractionListener {
        override fun onEdit(post: Post) {
            viewModel.edit(post)
            //newPostLauncher.launch(post.content)
            findNavController().navigate(
                R.id.action_feedFragment_to_newPostFragment,
                bundleOf("content" to post.content)
            )
        }

        override fun playVideo(post: Post) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
            startActivity(intent)
        }

        override fun onLike(post: Post) {
            viewModel.likeById()
        }

        override fun onView(post: Post) {
            viewModel.viewById(post.id)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post)
        }

        override fun onShare(post: Post) {
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

        override fun onPost(post: Post) {
            findNavController().navigate(R.id.action_feedFragment_to_postFragment, Bundle().apply {
                postId = post.id
            }
            )
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            binding.errorGroup.isVisible = state.error
            binding.empty.isVisible = state.empty
            binding.progress.isVisible = state.loading
            binding.swipeRefresh.isRefreshing = state.loading
            binding.swipeRefresh.setOnRefreshListener {
                setMenuVisibility(true)
            }
            adapter.submitList(state.posts)
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply {
                    textArg = ""
                }
            )
            //newPostLauncher.launch(null)
        }

        return binding.root

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