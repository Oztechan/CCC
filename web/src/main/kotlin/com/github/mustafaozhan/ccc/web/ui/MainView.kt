/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.web.ui

import com.github.mustafaozhan.ccc.client.di.getForJs
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.ccc.web.app.AppDependenciesContext
import react.RProps
import react.child
import react.dom.tr
import react.functionalComponent
import react.useContext

private val settingsRepository: SettingsRepository by lazy {
    useContext(AppDependenciesContext).koin.getForJs(SettingsRepository::class)
}

val MainView = functionalComponent<RProps> {
    kermit.d { "MainView" }
    child(
        functionalComponent {
            if (settingsRepository.firstRun) {
                tr { +"First Run" }
            } else {
                tr { +"Not first run" }
            }
        }
    )
}
