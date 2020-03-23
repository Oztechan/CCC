package mustafaozhan.github.com.mycurrencies.ui.main

import androidx.databinding.BindingAdapter
import com.google.android.gms.ads.AdView
import mustafaozhan.github.com.mycurrencies.extension.checkAd

@BindingAdapter("adId", "isEnabled")
fun adAdapter(adView: AdView, adId: String, isEnabled: Boolean) =
    adView.checkAd(adId, isEnabled)
