package mustafaozhan.github.com.mycurrencies.tool

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.gms.ads.AdView
import com.jaredrummler.materialspinner.MaterialSpinner
import mustafaozhan.github.com.mycurrencies.extension.checkAd
import mustafaozhan.github.com.mycurrencies.extension.setBackgroundByName
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorEvent

@BindingAdapter("adId", "isEnabled")
fun AdView.adAdapter(adId: String, isEnabled: Boolean) = checkAd(adId, isEnabled)

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("backgroundByName")
fun ImageView.backgroundByName(base: String) = setBackgroundByName(base)

@BindingAdapter("onItemSelected")
fun MaterialSpinner.onItemSelected(calculatorViewEvent: CalculatorEvent) =
    setOnItemSelectedListener { _, _, _, item ->
        calculatorViewEvent.onSpinnerItemSelected(item.toString())
    }
