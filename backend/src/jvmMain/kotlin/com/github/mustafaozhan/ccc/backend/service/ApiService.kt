package com.github.mustafaozhan.ccc.backend.service

import com.github.mustafaozhan.ccc.backend.controller.ApiController
import com.github.mustafaozhan.ccc.backend.di.koin
import com.github.mustafaozhan.ccc.common.di.getDependency

fun startListening(
    apiController: ApiController = koin.getDependency(ApiController::class)
) {
    apiController.startSyncApi()
}
