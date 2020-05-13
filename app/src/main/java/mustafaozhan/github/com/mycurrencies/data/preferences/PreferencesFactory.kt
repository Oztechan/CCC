/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.preferences

import android.content.Context
import com.github.mustafaozhan.basemob.preferences.BasePreferencesFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesFactory
@Inject constructor(
    context: Context
) : BasePreferencesFactory(context) {
    companion object {
        const val GENERAL_SHARED_PREFS = "GENERAL_SHARED_PREFS"
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS
}
