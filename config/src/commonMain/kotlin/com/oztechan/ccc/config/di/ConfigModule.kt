package com.oztechan.ccc.config.di

import com.oztechan.ccc.config.service.ad.AdConfigService
import com.oztechan.ccc.config.service.ad.AdConfigServiceImpl
import com.oztechan.ccc.config.service.review.ReviewConfigService
import com.oztechan.ccc.config.service.review.ReviewConfigServiceImpl
import com.oztechan.ccc.config.service.update.UpdateConfigService
import com.oztechan.ccc.config.service.update.UpdateConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val configModule = module {
    singleOf(::UpdateConfigServiceImpl) { bind<UpdateConfigService>() }
    singleOf(::AdConfigServiceImpl) { bind<AdConfigService>() }
    singleOf(::ReviewConfigServiceImpl) { bind<ReviewConfigService>() }
}
