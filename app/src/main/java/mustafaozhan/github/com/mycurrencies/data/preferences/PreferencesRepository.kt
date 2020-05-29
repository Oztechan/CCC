/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.preferences

import android.content.Context
import com.github.mustafaozhan.basemob.data.preferences.BasePreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Currencies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository
@Inject constructor(
    context: Context
) : BasePreferencesRepository(context) {
    companion object {
        const val GENERAL_SHARED_PREFS = "GENERAL_SHARED_PREFS"
        private const val MAIN_DATA = "MAIN_DATA"
        private const val FIRST_RUN = "firs_run"
        private const val CURRENT_BASE = "current_base"
        private const val AD_FREE_DATE = "ad_free_date"
        private const val DAY = 24 * 60 * 60 * 1000.toLong()
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS

    var firstRun
        get() = getValue(FIRST_RUN, getMainDataMap()?.get(0) == true.toString())
        set(value) = setValue(FIRST_RUN, value)

    var currentBase
        get() = getValue(CURRENT_BASE, getMainDataMap()?.get(1) ?: Currencies.EUR.toString())
        set(value) = setValue(CURRENT_BASE, value)

    var adFreeActivatedDate
        get() = getValue(AD_FREE_DATE, 0.toLong())
        set(value) = setValue(AD_FREE_DATE, value)

    fun isRewardExpired() = System.currentTimeMillis() - adFreeActivatedDate > DAY

    private fun getMainDataMap(): MutableList<String>? {
        val mainDataList: MutableList<String>? = null
        getValue(MAIN_DATA, "")
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
