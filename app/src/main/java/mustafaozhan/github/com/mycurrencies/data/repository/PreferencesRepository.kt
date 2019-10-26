package mustafaozhan.github.com.mycurrencies.data.repository

import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesHelper
import mustafaozhan.github.com.mycurrencies.model.MainData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject
constructor(private val sharedPreferencesHelper: PreferencesHelper) {

    fun loadMainData() = sharedPreferencesHelper.loadMainData()

    fun persistMainData(mainData: MainData) =
        sharedPreferencesHelper.persistMainData(mainData)

    fun loadResetData() = sharedPreferencesHelper.loadResetData()

    fun persistResetData(resetData: Boolean) = sharedPreferencesHelper.persistResetData(resetData)
}
