package ru.netology.nmedia.util

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object AndroidUtil {

    object AndroidUtils {
        fun hideKeyboard(view: View) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    object StringArg : ReadWriteProperty<Bundle, String?> {
        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }

        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? =
            thisRef.getString(property.name)
    }

    object LongArg : ReadWriteProperty<Bundle, Long> {
        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Long) {
            thisRef.putLong(property.name, value)
        }

        override fun getValue(thisRef: Bundle, property: KProperty<*>): Long =
            thisRef.getLong(property.name)
    }

    fun glideDownloadAttachUrl (url:String, view: View)  {
        Glide.with(view)
            .load(url)
            .error(R.drawable.ic_error_24)
            .fitCenter()
            .centerCrop()
            .centerInside()
            .timeout(10_000)
            .into(view as ImageView)
    }

    /*fun ImageView.load(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
        Glide.with(this)
            .load(url)
            .timeout(10_000)
            .transform(*transforms)
            .into(this)

    fun ImageView.loadCircleCrop(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
        load(url, CircleCrop(), *transforms)

     */
}