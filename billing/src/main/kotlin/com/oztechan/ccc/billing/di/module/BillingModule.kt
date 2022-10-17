package com.oztechan.ccc.billing.di.module

import com.oztechan.ccc.billing.BillingManager
import com.oztechan.ccc.billing.BillingManagerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val billingModule = module {
    singleOf(::BillingManagerImpl) { bind<BillingManager>() }
}
