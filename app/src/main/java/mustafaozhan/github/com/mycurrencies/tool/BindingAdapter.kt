package mustafaozhan.github.com.mycurrencies.tool

import androidx.databinding.BindingAdapter
import com.google.android.gms.ads.AdView
import mustafaozhan.github.com.mycurrencies.extension.checkAd

@BindingAdapter("adId", "isEnabled")
fun AdView.adAdapter(adId: String, isEnabled: Boolean) = checkAd(adId, isEnabled)
