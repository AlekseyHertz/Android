package ru.netology.nmedia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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


object NewPostContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?) =
        Intent(context, NewPostActivity::class.java).putExtra(Intent.EXTRA_TEXT, input)

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(Intent.EXTRA_TEXT)

}