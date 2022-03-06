package com.github.mustafaozhan.ccc.android.di

import com.github.mustafaozhan.ad.AdManager
import com.github.mustafaozhan.ad.AdManagerImpl
import com.mustafaozhan.github.analytics.AnalyticsManager
import com.mustafaozhan.github.analytics.AnalyticsManagerImpl
import com.oztechan.ccc.billing.BillingManager
import com.oztechan.ccc.billing.BillingManagerImpl
import org.koin.dsl.module

var platformModule = module {
    single<BillingManager> { BillingManagerImpl(get()) }
    single<AdManager> { AdManagerImpl() }
    single<AnalyticsManager> { AnalyticsManagerImpl(get()) }
}
