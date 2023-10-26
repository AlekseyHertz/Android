package ru.netology.nmedia.viewmodel // из NewPostActivity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.repository.Helper

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by Helper.StringArg
    }

    private val viewModel: PostViewModel by activityViewModels()

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            when (it.resultCode) {
//                ImagePicker.RESULT_ERROR -> {
//                    Snackbar.make(
//                        binding.root,
//                        ImagePicker.getError(it.data),
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                }
//
//                Activity.RESULT_OK -> {
//                    val url: Uri? = it.data?.data
//                    viewModel.setPhoto(uri, uri?.toFile())
//                }
//            }
//
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            val uri = requireNotNull(it.data?.data)
            val file = uri.toFile()

            viewModel.setPhoto(uri, file)
        }

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

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.save_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId) {
                        R.id.save -> {
                            viewModel.changeContent(binding.edit.text.toString())
                            viewModel.save()
                            Helper.AndroidUtils.hideKeyboard(requireView())
                            true
                        }

                        else -> false
                    }
            },
            viewLifecycleOwner,
        )

        viewModel.photo.observe(viewLifecycleOwner) { photo ->
            if (photo == null) {
                binding.photoContainer.isVisible = true
                return@observe
            }

            binding.photoContainer.isVisible = true
            binding.photo.setImageURI(photo.uri)
        }

        binding.gallery.setOnClickListener {
            ImagePicker.Builder(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg",
                    )
                )
//                .galleryOnly()
//                .maxResultSize(2048, 2048)
                .createIntent(photoLauncher::launch)
        }

        binding.takePhoto.setOnClickListener {
            ImagePicker.Builder(this)
                //.galleryOnly()
                .crop()
                //.maxResultSize(2048, 2048)
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(photoLauncher::launch)
        }

        binding.removePhoto.setOnClickListener {
            viewModel.clearPhoto(null, null)
        }

        return binding.root
    }
}