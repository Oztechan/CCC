package mustafaozhan.github.com.mycurrencies.data.repository

import mustafaozhan.github.com.mycurrencies.data.preferences.GeneralSharedPreferences
import mustafaozhan.github.com.mycurrencies.model.MainData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject
constructor(private val generalSharedPreferences: GeneralSharedPreferences) {

    fun loadMainData() = generalSharedPreferences.loadMainData()

    fun persistMainData(mainData: MainData) =
        generalSharedPreferences.persistMainData(mainData)

    fun loadResetData() = generalSharedPreferences.loadResetData()

    fun persistResetData(resetData: Boolean) = generalSharedPreferences.persistResetData(resetData)
}
