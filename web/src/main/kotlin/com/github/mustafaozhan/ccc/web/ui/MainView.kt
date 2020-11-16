/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.web.ui

import com.github.mustafaozhan.ccc.client.di.getForJs
import com.github.mustafaozhan.ccc.client.main.MainViewModel
import com.github.mustafaozhan.ccc.web.app.AppDependenciesContext
import com.github.mustafaozhan.ccc.web.app.kermit
import react.RProps
import react.child
import react.dom.h1
import react.dom.tr
import react.functionalComponent
import react.useContext

private val mainViewModel: MainViewModel by lazy {
    useContext(AppDependenciesContext).koin.getForJs(MainViewModel::class)
}

val MainView = functionalComponent<RProps> {
    kermit.d { "MainView" }
    child(
        functionalComponent {
            h1 { +mainViewModel.getAppName() }
            tr { +mainViewModel.getPlatformName() }
            tr { +"${mainViewModel.runCounter} times ran" }
        }
    )
}
