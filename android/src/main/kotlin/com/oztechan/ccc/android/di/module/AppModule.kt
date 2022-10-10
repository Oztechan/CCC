package com.oztechan.ccc.android.di.module

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.AnalyticsManagerImpl
import com.oztechan.ccc.billing.BillingManager
import com.oztechan.ccc.billing.BillingManagerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal var appModule = module {
    singleOf(::BillingManagerImpl) { bind<BillingManager>() }
    singleOf(::AnalyticsManagerImpl) { bind<AnalyticsManager>() }
}
