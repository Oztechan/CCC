package com.github.mustafaozhan.ad

import android.content.Context
import com.google.android.gms.ads.MobileAds

fun initAds(context: Context) {
    MobileAds.initialize(context)
}
