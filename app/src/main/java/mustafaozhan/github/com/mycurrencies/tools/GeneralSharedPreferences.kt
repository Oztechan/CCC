package mustafaozhan.github.com.mycurrencies.tools

import mustafaozhan.github.com.mycurrencies.base.BaseSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
@Singleton
class GeneralSharedPreferences @Inject
constructor() : BaseSharedPreferences() {

    companion object {
        const val GENERAL_SHARED_PREFS = "GENERAL_SHARED_PREFS"
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS

}