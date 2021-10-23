package com.github.mustafaozhan.ccc.android.di

import com.github.mustafaozhan.ad.AdManager
import com.github.mustafaozhan.ad.AdManagerImpl
import com.github.mustafaozhan.billing.BillingManager
import com.github.mustafaozhan.billing.BillingManagerImpl
import org.koin.dsl.module

var platformModule = module {
    single<BillingManager> { BillingManagerImpl(get()) }
    single<AdManager> { AdManagerImpl(get()) }
}
