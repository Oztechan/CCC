/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.splash

import android.content.Intent
import android.os.Bundle
import com.github.mustafaozhan.basemob.view.activity.BaseActivity
import com.github.mustafaozhan.ui.main.MainActivity
import com.github.mustafaozhan.ui.slider.SliderActivity
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashViewModel.preferencesRepository.syncPreferences()

        startActivity(
            Intent(
                this,
                if (splashViewModel.preferencesRepository.firstRun) {
                    SliderActivity::class.java
                } else {
                    MainActivity::class.java
                }
            )
        )

        finish()
    }
}
