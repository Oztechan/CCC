package mustafaozhan.github.com.mycurrencies.repository.data

import com.google.gson.Gson
import mustafaozhan.github.com.mycurrencies.base.BaseSharedPreferences
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.MainData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject
constructor() : BaseSharedPreferences() {

    companion object {
        const val GENERAL_SHARED_PREFS = "GENERAL_SHARED_PREFS"
        const val MAIN_DATA = "MAIN_DATA"
        const val RESET_DATA = "RESET_DATA"
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS

    fun persistMainData(mainData: MainData) {
        setStringEntry(MAIN_DATA, Gson().toJson(mainData))
    }

    fun loadMainData() =
        Gson().fromJson(getStringEntry(MAIN_DATA), MainData::class.java)
            ?: MainData(
                true,
                Currencies.EUR,
                null,
                null
            )

    fun loadResetData() = getBooleanEntry(RESET_DATA, true)

    fun persistResetData(resetData: Boolean) = setBooleanEntry(RESET_DATA, resetData)
}
