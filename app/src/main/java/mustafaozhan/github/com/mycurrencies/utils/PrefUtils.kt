package mustafaozhan.github.com.mycurrencies.utils

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Mustafa Ozhan on 11/17/17 at 10:09 PM on Arch Linux.
 */
fun getString(context: Context, key: String, defValue: String): String {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getString(key, defValue)
}

fun putString(context: Context, key: String, value: String) {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = pref.edit()
    editor.putString(key, value)
    editor.apply()
}

fun getLong(context: Context, key: String, defValue: Long): Long {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getLong(key, defValue)
}

fun putLong(context: Context, key: String, value: Long) {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = pref.edit()
    editor.putLong(key, value)
    editor.apply()
}

fun exists(context: Context, key: String): Boolean {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.contains(key)
}

fun remove(context: Context, key: String) {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = pref.edit()
    editor.remove(key)
    editor.apply()
}