/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.github.mustafaozhan.basemob.activity.BaseActivity
import com.github.mustafaozhan.ccc.android.ui.main.MainActivity
import com.github.mustafaozhan.ccc.android.ui.slider.SliderActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {

    private val vm: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(vm.useCase.getAppTheme())

        startActivity(
            Intent(
                this,
                if (vm.useCase.isFirstRun()) SliderActivity::class.java else MainActivity::class.java
            )
        )

        finish()
    }
}
