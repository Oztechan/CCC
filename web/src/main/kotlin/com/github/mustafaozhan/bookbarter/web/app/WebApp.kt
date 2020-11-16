/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.bookbarter.web.app

import com.github.mustafaozhan.bookbarter.client.di.getForJs
import com.github.mustafaozhan.bookbarter.client.di.initJS
import com.github.mustafaozhan.bookbarter.web.ui.MainView
import kotlinx.browser.document
import kotlinx.browser.localStorage
import react.child
import react.dom.render

private const val ROOT_ID = "root"
//val AppDependenciesContext = createContext<KoinApplication>()
//
//val kermit: Kermit by lazy {
//    useContext(AppDependenciesContext).koin.getForJs(Kermit::class)
//}

fun main() {
    render(document.getElementById(ROOT_ID)) {
        AppDependenciesContext.Provider(
            initJS(localStorage)
        ) {
            child(MainView)
        }
    }
}
