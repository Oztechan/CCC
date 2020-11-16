/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.base.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding

abstract class BaseVBActivity<TViewBinding : ViewBinding> : BaseActivity() {

    protected lateinit var binding: TViewBinding

    abstract fun bind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        setContentView(binding.root)
    }
}
