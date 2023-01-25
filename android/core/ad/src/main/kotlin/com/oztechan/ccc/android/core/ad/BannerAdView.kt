package com.oztechan.ccc.android.core.ad

import android.content.Context
import android.view.View
import android.widget.FrameLayout

class BannerAdView(
    context: Context,
    banner: View,
    val onDestroy: () -> Unit
) : FrameLayout(context) {
    init {
        this.addView(banner)
    }
}
