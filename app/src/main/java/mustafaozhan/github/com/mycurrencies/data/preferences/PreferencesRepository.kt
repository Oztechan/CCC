/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.preferences

import com.github.mustafaozhan.basemob.preferences.BasePreferencesRepository
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.MainData
import org.joda.time.Duration
import org.joda.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository
@Inject constructor(
    override val preferencesHelper: PreferencesHelper
) : BasePreferencesRepository() {

    companion object {
        const val MAIN_DATA = "MAIN_DATA"
        private const val NUMBER_OF_HOURS = 24
    }

    override val moshi: Moshi
        get() = Moshi.Builder().build()

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

    fun loadMainData() = preferencesHelper.getValue(
        MAIN_DATA,
        moshi.adapter(MainData::class.java).toJson(MainData())
    ).let {
        moshi.adapter(MainData::class.java)
            .fromJson(it) ?: MainData()
    }

    private fun persistMainData(mainData: MainData) = preferencesHelper.setValue(
        MAIN_DATA,
        moshi.adapter(MainData::class.java).toJson(mainData)
    )

    fun updateMainData(
        firstRun: Boolean? = null,
        currentBase: Currencies? = null,
        adFreeActivatedDate: Instant? = null
    ) {
        val mainData = loadMainData()
        firstRun?.let { mainData.firstRun = it }
        currentBase?.let { mainData.currentBase = it }
        adFreeActivatedDate?.let { mainData.adFreeActivatedDate = it }
        persistMainData(mainData)
    }
}
