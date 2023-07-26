package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityVideoBinding

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.videoPic)


        binding.videoPic.setOnClickListener {
            Intent.ACTION_VIEW
        }
        intent?.let {
            if (it.action != Intent.ACTION_VIEW) {
                return@let
            }


            /*fun playMedia(file: Uri) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = file
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }*/
        }
    }
}

/*object VideoContract: ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit): Intent (context, VideoActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?) = intent?.getExtra(Intent.EXTRA_VIEW)
    }
}*/