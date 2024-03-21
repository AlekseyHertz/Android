package ru.netology.nmedia.activity // из MainActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.adapter.PostLoadingStateAdapter
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.vi.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    private val viewModel: PostViewModel by activityViewModels()
    val authViewModel: AuthViewModel by activityViewModels()

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

        authViewModel.data.observe(viewLifecycleOwner) {
            requireActivity().invalidateOptionsMenu()
        }

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
                if (authViewModel.authorized) {
                    viewModel.likeById(post)
                } else {
                    findNavController().navigate(R.id.action_feedFragment_to_fragment_auth)
                }
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onPost(post: Post) {
                if (authViewModel.authorized) {
                    findNavController().navigate(
                        R.id.action_feedFragment_to_postFragment,
                        Bundle().apply {
                            putLong("postId", post.id)
                        }
                    )
                } else {
                    findNavController().navigate(R.id.action_feedFragment_to_fragment_auth)
                }
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, "Share post")
                startActivity(shareIntent)
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

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PostLoadingStateAdapter { adapter.retry() },
            footer = PostLoadingStateAdapter { adapter.retry() }
        )

        @Suppress("DEPRECATION")
        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }

        @Suppress("DEPRECATION")
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swipeRefresh.isRefreshing = it.refresh is LoadState.Loading
                        || it.append is LoadState.Loading
                        || it.prepend is LoadState.Loading
            }
        }

        binding.swipeRefresh.setOnRefreshListener(adapter::refresh)

        binding.add.setOnClickListener {
            if (authViewModel.authorized) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
            } else {
                findNavController().navigate(R.id.action_feedFragment_to_fragment_auth)
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { state ->
                    binding.swipeRefresh.isRefreshing =
                        state.refresh is LoadState.Loading
                }
            }
        }

        // устаревший вариант кода
        /*viewModel.state.observe(viewLifecycleOwner) { state ->
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
        }*/

        /*viewModel.newerCount.observe(viewLifecycleOwner) {
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
        }*/

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