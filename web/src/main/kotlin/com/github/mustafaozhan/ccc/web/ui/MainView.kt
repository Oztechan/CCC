/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.web.ui

import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.web.app.AppDependenciesContext
import react.RProps
import react.child
import react.dom.tr
import react.functionalComponent
import react.useContext

private val settingsRepository: SettingsRepository by lazy {
    useContext(AppDependenciesContext).koin.getDependency(SettingsRepository::class)
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
