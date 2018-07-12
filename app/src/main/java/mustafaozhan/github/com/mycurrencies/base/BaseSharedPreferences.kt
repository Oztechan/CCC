package mustafaozhan.github.com.mycurrencies.base

import android.content.Context
import android.content.SharedPreferences
import mustafaozhan.github.com.mycurrencies.application.Application

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:42 PM on Arch Linux wit Love <3.
 */
abstract class BaseSharedPreferences {

    protected abstract val preferencesName: String

    protected val preferencesEditor: SharedPreferences.Editor
        get() = getPreferencesEditor(preferencesName)

    protected val sharedPreferences: SharedPreferences
        get() = getSharedPreferences(preferencesName)

    protected fun getPreferencesEditor(preferencesName: String): SharedPreferences.Editor {

        val prefs = Application.instance.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return prefs.edit()
    }

    protected fun getSharedPreferences(preferencesName: String): SharedPreferences {
        return Application.instance.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }
}