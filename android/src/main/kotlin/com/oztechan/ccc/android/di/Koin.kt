package com.oztechan.ccc.android.di

import android.content.Context
import co.touchlab.kermit.Logger
import com.oztechan.ccc.ad.di.module.adModule
import com.oztechan.ccc.analytics.di.module.analyticsModule
import com.oztechan.ccc.billing.di.module.billingModule
import com.oztechan.ccc.client.di.module.clientModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun initKoin(context: Context) = startKoin {
    androidContext(context)

    modules(
        buildList {
            add(adModule)
            add(analyticsModule)
            add(billingModule)
            addAll(clientModules)
        }
    )
}.also {
    Logger.i { "Koin initialised" }
}
