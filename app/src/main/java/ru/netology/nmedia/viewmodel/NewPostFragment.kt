package ru.netology.nmedia.viewmodel // из NewPostActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.repository.Helper

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by Helper.StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstantState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg
            ?.let(binding.edit::setText)

        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            Helper.AndroidUtils.hideKeyboard(requireView())
        //findNavController().navigateUp()
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        /*viewModel.edited.observe(viewLifecycleOwner) {
            post ->
            binding.edit.setText(post.content)
            Log.d("newPostFragment", "edit")
        }

         */

        return binding.root
    }
}
/*override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityNewPostBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val content = intent?.getStringExtra(Intent.EXTRA_TEXT)?:""
    binding.content.setText(content)

    val activity = this
    activity.onBackPressedDispatcher.addCallback(
        activity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(AppCompatActivity.RESULT_CANCELED, intent)
                finish()
            }
        }
    )

    binding.ok.setOnClickListener {
        val text = binding.content.text.toString() //
        if (text.isBlank()) {
            setResult(RESULT_CANCELED)
        } else {
            setResult(RESULT_OK, Intent(). apply { putExtra(Intent.EXTRA_TEXT,text) })
        }
        finish() //
    }
}
}


/*object NewPostContract : ActivityResultContract<String?, String?>() {
override fun createIntent(context: Context, input: String?) =
    Intent(context, NewPostActivity::class.java).putExtra(Intent.EXTRA_TEXT, input)

override fun parseResult(resultCode: Int, intent: Intent?) =
    intent?.getStringExtra(Intent.EXTRA_TEXT)

}*/