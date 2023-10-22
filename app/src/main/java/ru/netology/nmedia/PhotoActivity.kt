package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.ActivityPhotoBinding

class PhotoActivity : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ActivityPhotoBinding.inflate(inflater, container, false)


        binding.photoPic.setOnClickListener {
            Intent.ACTION_VIEW
        }
        binding.picBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}

/*object VideoContract: ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit): Intent (context, VideoActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?) = intent?.getExtra(Intent.EXTRA_VIEW)
    }
}*/