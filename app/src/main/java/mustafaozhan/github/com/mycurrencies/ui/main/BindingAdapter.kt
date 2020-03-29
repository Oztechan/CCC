package mustafaozhan.github.com.mycurrencies.ui.main

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.gms.ads.AdView
import mustafaozhan.github.com.mycurrencies.extension.checkAd
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName

@BindingAdapter("adId", "isEnabled")
fun adAdapter(adView: AdView, adId: String, isEnabled: Boolean) =
    adView.checkAd(adId, isEnabled)

@BindingAdapter("setBackgroundByName")
fun setBackgroundByName(
    imageView: ImageView,
    base: String
) = imageView.setBackgroundByName(base)
