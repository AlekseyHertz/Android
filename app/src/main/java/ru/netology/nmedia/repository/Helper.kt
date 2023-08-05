package ru.netology.nmedia.repository

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Helper {

    object AndroidUtils {
        fun hideKeyboard(view: View) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
}