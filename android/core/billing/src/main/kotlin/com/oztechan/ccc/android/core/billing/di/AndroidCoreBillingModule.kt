package com.oztechan.ccc.android.core.billing.di

import com.oztechan.ccc.android.core.billing.BillingManager
import com.oztechan.ccc.android.core.billing.BillingManagerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidCoreBillingModule = module {
    singleOf(::BillingManagerImpl) { bind<BillingManager>() }
}
