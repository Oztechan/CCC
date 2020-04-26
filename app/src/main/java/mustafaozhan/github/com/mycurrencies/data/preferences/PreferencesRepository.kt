package mustafaozhan.github.com.mycurrencies.data.preferences

import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.MainData
import org.joda.time.Duration
import org.joda.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository
@Inject constructor(
    private val sharedPreferencesHelper: PreferencesHelper
) {

    companion object {
        private const val NUMBER_OF_HOURS = 24
    }

    var currentBase: String = loadMainData().currentBase.toString()
        get() = loadMainData().currentBase.toString()
        set(value) {
            updateMainData(
                currentBase = enumValues<Currencies>()
                    .find { it.name == value }
                    ?: Currencies.NULL
            )
            field = value
        }

    val firstRun
        get() = loadMainData().firstRun

    val isRewardExpired: Boolean
        get() = loadMainData().adFreeActivatedDate?.let {
            Duration(it, Instant.now()).standardHours > NUMBER_OF_HOURS
        } ?: true

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
}
