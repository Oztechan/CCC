package com.github.mustafaozhan.ccc.android.di

import android.content.Context
import com.github.mustafaozhan.ad.AdManager
import com.github.mustafaozhan.ad.AdManagerImpl
import com.github.mustafaozhan.billing.BillingManager
import com.github.mustafaozhan.billing.BillingManagerImpl
import org.koin.dsl.module

fun getPlatformModule(context: Context) = module {
    single<BillingManager> { BillingManagerImpl(get()) }
    single<AdManager> {
        AdManagerImpl().also {
            it.initMobileAds(context)
        }
    }
}
