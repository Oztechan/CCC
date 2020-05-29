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
        private const val MAIN_DATA = "MAIN_DATA"
        private const val FIRST_RUN = "firs_run"
        private const val CURRENT_BASE = "current_base"
        private const val AD_FREE_DATE = "ad_free_date"
        private const val DAY = 24 * 60 * 60 * 1000.toLong()
    }

    var firstRun
        get() = preferencesFactory.getValue(FIRST_RUN, getMainDataMap()?.get(0) == true.toString())
        set(value) = preferencesFactory.setValue(FIRST_RUN, value)

    var currentBase
        get() = preferencesFactory.getValue(
            CURRENT_BASE,
            getMainDataMap()?.get(1) ?: Currencies.EUR.toString()
        )
        set(value) = preferencesFactory.setValue(CURRENT_BASE, value)

    var adFreeActivatedDate
        get() = preferencesFactory.getValue(AD_FREE_DATE, 0.toLong())
        set(value) = preferencesFactory.setValue(AD_FREE_DATE, value)

    fun isRewardExpired() = System.currentTimeMillis() - adFreeActivatedDate > DAY

    private fun getMainDataMap(): MutableList<String>? {
        val mainDataList: MutableList<String>? = null
        preferencesFactory.getValue(MAIN_DATA, "")
            .dropLast(1).drop(1)
            .replace("\"", "")
            .split(",")
            .take(2)
            .forEach { value ->
                value.split(":").let {
                    mainDataList?.add(it[1])
                }
            }
        return mainDataList
    }
}
