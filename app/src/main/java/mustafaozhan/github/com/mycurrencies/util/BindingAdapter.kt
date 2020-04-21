package mustafaozhan.github.com.mycurrencies.util

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.gms.ads.AdView
import com.jaredrummler.materialspinner.MaterialSpinner
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.extension.checkAd
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorEvent

@BindingAdapter("adId", "isEnabled")
fun AdView.adAdapter(adId: String, isEnabled: Boolean) = checkAd(adId, isEnabled)

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean) = if (visible) {
    bringToFront()
    visibility = View.VISIBLE
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
} else {
    visibility = View.GONE
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
}

@BindingAdapter("backgroundByName")
fun ImageView.backgroundByName(base: String) = setBackgroundByName(base)

@BindingAdapter("onItemSelected")
fun MaterialSpinner.onItemSelected(calculatorViewEvent: CalculatorEvent) =
    setOnItemSelectedListener { _, _, _, item ->
        calculatorViewEvent.onSpinnerItemSelected(item.toString())
    }
