/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.content.Intent
import android.os.Bundle
import com.github.mustafaozhan.basemob.activity.BaseActivity
import com.github.mustafaozhan.logmob.kermit

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kermit.d { "SplashActivity onCreate" }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
