/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.preferences

import android.content.Context
import com.github.mustafaozhan.basemob.preferences.BasePreferencesHelper
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.model.MainData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper
@Inject constructor(
    context: Context
) : BasePreferencesHelper(context) {
    companion object {
        const val GENERAL_SHARED_PREFS = "GENERAL_SHARED_PREFS"
        const val MAIN_DATA = "MAIN_DATA"
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS

    override val moshi: Moshi
        get() = Moshi.Builder().build()

    fun persistMainData(mainData: MainData) = setValue(
        MAIN_DATA,
        moshi.adapter(MainData::class.java).toJson(mainData)
    )

    fun loadMainData() = getValue(
        MAIN_DATA,
        moshi.adapter(MainData::class.java).toJson(MainData())
    ).let {
        moshi.adapter(MainData::class.java)
            .fromJson(it) ?: MainData()
    }
}
