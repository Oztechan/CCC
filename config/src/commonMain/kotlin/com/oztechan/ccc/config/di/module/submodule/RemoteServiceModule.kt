package com.oztechan.ccc.config.di.module.submodule

import com.oztechan.ccc.config.AppConfigService
import com.oztechan.ccc.config.AppConfigServiceImpl
import com.oztechan.ccc.config.ad.AdConfigService
import com.oztechan.ccc.config.ad.AdConfigServiceImpl
import com.oztechan.ccc.config.review.ReviewConfigService
import com.oztechan.ccc.config.review.ReviewConfigServiceImpl
import com.oztechan.ccc.config.update.UpdateConfigService
import com.oztechan.ccc.config.update.UpdateConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val configServiceModule = module {
    singleOf(::UpdateConfigServiceImpl) { bind<UpdateConfigService>() }
    singleOf(::AdConfigServiceImpl) { bind<AdConfigService>() }
    singleOf(::ReviewConfigServiceImpl) { bind<ReviewConfigService>() }
    singleOf(::AppConfigServiceImpl) { bind<AppConfigService>() }
}
