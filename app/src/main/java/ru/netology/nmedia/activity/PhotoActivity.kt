package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.BuildConfig.BASE_URL
import ru.netology.nmedia.databinding.ActivityPhotoBinding
import ru.netology.nmedia.util.AndroidUtil.glideDownloadAttachUrl

class PhotoActivity : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ActivityPhotoBinding.inflate(
            inflater,
            container,
            false
        )

        val serverPathUrl = "${BASE_URL}"
        val attachmentsUrl = "${serverPathUrl}/media"
        val attachmentUrl = arguments?.getString("attachUrl")
        val downloadAttachUrl = "${attachmentsUrl}/${attachmentUrl}"
        glideDownloadAttachUrl(downloadAttachUrl,binding.photoPic)
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