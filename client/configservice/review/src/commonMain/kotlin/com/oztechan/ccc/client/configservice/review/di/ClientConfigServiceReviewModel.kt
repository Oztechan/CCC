package com.oztechan.ccc.client.configservice.review.di

import com.oztechan.ccc.client.configservice.review.ReviewConfigService
import com.oztechan.ccc.client.configservice.review.ReviewConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientConfigServiceReviewModel = module {
    singleOf(::ReviewConfigServiceImpl) { bind<ReviewConfigService>() }
}
