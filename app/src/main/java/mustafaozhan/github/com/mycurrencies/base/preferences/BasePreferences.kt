package mustafaozhan.github.com.mycurrencies.base.preferences

import android.content.Context
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.app.CCCApplication

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:42 PM on Arch Linux wit Love <3.
 */
@Suppress("SameParameterValue")
abstract class BasePreferences {

    protected abstract val preferencesName: String
    protected abstract val moshi: Moshi

    private val preferences
        get() = CCCApplication.instance
            .getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

    private val editor
        get() = preferences.edit()

    protected fun getStringEntry(key: String, defaultValue: String) = preferences
        .getString(key, defaultValue) ?: defaultValue

    protected fun setStringEntry(key: String, value: String) = editor
        .putString(key, value)
        .commit()

    protected fun setBooleanEntry(key: String, value: Boolean) = editor
        .putBoolean(key, value)
        .commit()

    protected fun getBooleanEntry(key: String, defaultValue: Boolean = false) = preferences
        .getBoolean(key, defaultValue)
}
