package ru.netology.nmedia.activity // из MainActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.activity.PostFragment.Companion.postId
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                //newPostLauncher.launch(post.content)
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    bundleOf().apply {
                        textArg = post.content
                    }
                )
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onPost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment,
                    Bundle().apply {
                        postId = post.id
                    }
                )
            }

            override fun onImage(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_photoActivity,
                    Bundle().apply {
                        putString("attachUrl", post.attachment?.url)
                    }
                )
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner)
        { state ->
            binding.empty.isVisible = state.empty
            binding.retry.setOnClickListener {
                viewModel.loadPosts()
            }
            adapter.submitList(state.posts)

            binding.add.setOnClickListener {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = ""
                    }
                )
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
//            binding.errorGroup.isVisible = state.error
            binding.progress.isVisible = state.loading
            binding.swipeRefresh.isRefreshing = state.refreshing
            binding.swipeRefresh.setOnClickListener {
                viewModel.refresh()
            }
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_download, Snackbar.LENGTH_LONG)
                    .setAction(R.string.repead) { viewModel.loadPosts() }
                    .show()
            }
        }

        viewModel.newerCount.observe(viewLifecycleOwner) {
            Log.d("FeedFragment", "Newer count: $it")
            if (it > 0) {
                binding.loadPost.visibility = View.VISIBLE
                binding.loadPost.text = "Новых постов: $it"
            } else {
                binding.loadPost.visibility = View.GONE
            }
            binding.loadPost.setOnClickListener {
                viewModel.refresh()
                binding.loadPost.visibility = View.GONE
            }
        }

        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        })

        return binding.root
    }
}