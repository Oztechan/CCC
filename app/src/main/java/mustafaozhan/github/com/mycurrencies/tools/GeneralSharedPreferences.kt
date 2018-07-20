package mustafaozhan.github.com.mycurrencies.tools

import com.google.gson.Gson
import mustafaozhan.github.com.mycurrencies.base.BaseSharedPreferences
import mustafaozhan.github.com.mycurrencies.base.model.MainData
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
        const val MAIN_DATA = "MAIN_DATA"
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS

    fun persistMainData(mainData: MainData) {
        setStringEntry(MAIN_DATA, Gson().toJson(mainData))
    }

    fun loadMainData(): MainData {
        val mainDataJson = getStringEntry(MAIN_DATA)
        return Gson().fromJson(mainDataJson, MainData::class.java)
                ?: MainData(true, Currencies.EUR, Currencies.EUR)
    }
}