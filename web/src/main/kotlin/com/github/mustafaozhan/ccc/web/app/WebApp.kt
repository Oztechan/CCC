/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.web.app

import co.touchlab.kermit.Kermit
import com.github.mustafaozhan.ccc.client.di.getForJs
import com.github.mustafaozhan.ccc.client.di.initJS
import com.github.mustafaozhan.ccc.web.ui.MainView
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.koin.core.KoinApplication
import react.child
import react.createContext
import react.dom.render
import react.useContext

private const val ROOT_ID = "root"
val AppDependenciesContext = createContext<KoinApplication>()

val kermit: Kermit by lazy {
    useContext(AppDependenciesContext).koin.getForJs(Kermit::class)
}

fun main() {
    render(document.getElementById(ROOT_ID)) {
        AppDependenciesContext.Provider(
            initJS(localStorage)
        ) {
            child(MainView)
        }
    }
}
