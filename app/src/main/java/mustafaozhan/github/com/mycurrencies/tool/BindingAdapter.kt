package mustafaozhan.github.com.mycurrencies.tool

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.gms.ads.AdView
import mustafaozhan.github.com.mycurrencies.extension.checkAd

@BindingAdapter("adId", "isEnabled")
fun AdView.adAdapter(adId: String, isEnabled: Boolean) = checkAd(adId, isEnabled)

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
