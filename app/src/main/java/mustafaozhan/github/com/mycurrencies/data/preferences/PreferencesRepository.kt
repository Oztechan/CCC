package mustafaozhan.github.com.mycurrencies.data.preferences

import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.MainData
import org.joda.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject
constructor(private val sharedPreferencesHelper: PreferencesHelper) {

    fun loadMainData() = sharedPreferencesHelper.loadMainData()

    private fun persistMainData(mainData: MainData) =
        sharedPreferencesHelper.persistMainData(mainData)

    fun updateMainData(
        firstRun: Boolean? = null,
        currentBase: Currencies? = null,
        adFreeActivatedDate: Instant? = null,
        sliderShown: Boolean? = null
    ) {
        val mainData = loadMainData()
        firstRun?.let { mainData.firstRun = it }
        currentBase?.let { mainData.currentBase = it }
        adFreeActivatedDate?.let { mainData.adFreeActivatedDate = it }
        sliderShown?.let { mainData.sliderShown = it }
        persistMainData(mainData)
    }

    fun loadResetData() = sharedPreferencesHelper.loadResetData()

    fun persistResetData(resetData: Boolean) = sharedPreferencesHelper.persistResetData(resetData)
}
