/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.preferences

import com.github.mustafaozhan.basemob.data.preferences.BasePreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Currencies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository
@Inject constructor(
    override val preferencesFactory: PreferencesFactory
) : BasePreferencesRepository {

    companion object {
        const val MAIN_DATA = "MAIN_DATA"
        const val FIRST_RUN = "first_run"
        const val CURRENT_BASE = "current_base"
        const val AD_FREE_ACTIVATION_DATE = "ad_free_activation_date"
        private const val NUMBER_OF_HOURS = 24
    }

    var isFirstRun
        get() = preferencesFactory.getValue(FIRST_RUN, true)
        set(value) = preferencesFactory.setValue(FIRST_RUN, value)

    var currentBase
        get() = preferencesFactory.getValue(CURRENT_BASE, Currencies.EUR.toString())
        set(value) = preferencesFactory.setValue(CURRENT_BASE, value)

    var adFreeActivatedDate
        get() = preferencesFactory.getValue(AD_FREE_ACTIVATION_DATE, System.currentTimeMillis())
        set(value) = preferencesFactory.setValue(AD_FREE_ACTIVATION_DATE, value)

//    override val moshi: Moshi
//        get() = Moshi.Builder().build()
//
//    var currentBase: String = loadMainData().currentBase.toString()
//        get() = loadMainData().currentBase.toString()
//        set(value) {
//            updateMainData(
//                currentBase = enumValues<Currencies>()
//                    .find { it.name == value }
//                    ?: Currencies.NULL
//            )
//            field = value
//        }
//
//    val firstRun
//        get() = loadMainData().firstRun
//
//    val isRewardExpired: Boolean
//        get() = loadMainData().adFreeActivatedDate?.let {
//            Duration(it, Instant.now()).standardHours > NUMBER_OF_HOURS
//        } ?: true
//
//    fun loadMainData() = preferencesFactory.getValue(
//        MAIN_DATA,
//        moshi.adapter(MainData::class.java).toJson(MainData())
//    ).let {
//        moshi.adapter(MainData::class.java)
//            .fromJson(it) ?: MainData()
//    }
//
//    private fun persistMainData(mainData: MainData) = preferencesFactory.setValue(
//        MAIN_DATA,
//        moshi.adapter(MainData::class.java).toJson(mainData)
//    )
//
//    fun updateMainData(
//        firstRun: Boolean? = null,
//        currentBase: Currencies? = null,
//        adFreeActivatedDate: Instant? = null
//    ) {
//        val mainData = loadMainData()
//        firstRun?.let { mainData.firstRun = it }
//        currentBase?.let { mainData.currentBase = it }
//        adFreeActivatedDate?.let { mainData.adFreeActivatedDate = it }
//        persistMainData(mainData)
//    }
}
