/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.bookbarter.web.ui

import com.github.mustafaozhan.bookbarter.client.di.getForJs
import com.github.mustafaozhan.bookbarter.client.main.MainViewModel
import com.github.mustafaozhan.bookbarter.web.app.AppDependenciesContext
import com.github.mustafaozhan.bookbarter.web.app.kermit
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
