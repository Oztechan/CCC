package mustafaozhan.github.com.mycurrencies.extensions

import android.annotation.SuppressLint
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import mustafaozhan.github.com.mycurrencies.BuildConfig
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivity
import mustafaozhan.github.com.mycurrencies.main.fragment.MainFragment
import mustafaozhan.github.com.mycurrencies.settings.SettingsFragment

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
fun ImageView.setBackgroundByName(name: String) {
    when (name) {
        "EUR" -> this.setImageResource(R.drawable.eur)
        "AUD" -> this.setImageResource(R.drawable.aud)
        "BGN" -> this.setImageResource(R.drawable.bgn)
        "BRL" -> this.setImageResource(R.drawable.brl)
        "CAD" -> this.setImageResource(R.drawable.cad)
        "CHF" -> this.setImageResource(R.drawable.chf)
        "CNY" -> this.setImageResource(R.drawable.cny)
        "CZK" -> this.setImageResource(R.drawable.czk)
        "DKK" -> this.setImageResource(R.drawable.dkk)
        "GBP" -> this.setImageResource(R.drawable.gbp)
        "HKD" -> this.setImageResource(R.drawable.hkd)
        "HRK" -> this.setImageResource(R.drawable.hrk)
        "HUF" -> this.setImageResource(R.drawable.huf)
        "IDR" -> this.setImageResource(R.drawable.idr)
        "ILS" -> this.setImageResource(R.drawable.ils)
        "INR" -> this.setImageResource(R.drawable.inr)
        "JPY" -> this.setImageResource(R.drawable.jpy)
        "KRW" -> this.setImageResource(R.drawable.krw)
        "MXN" -> this.setImageResource(R.drawable.mxn)
        "MYR" -> this.setImageResource(R.drawable.myr)
        "NOK" -> this.setImageResource(R.drawable.nok)
        "NZD" -> this.setImageResource(R.drawable.nzd)
        "PHP" -> this.setImageResource(R.drawable.php)
        "PLN" -> this.setImageResource(R.drawable.pln)
        "RON" -> this.setImageResource(R.drawable.ron)
        "RUB" -> this.setImageResource(R.drawable.rub)
        "SEK" -> this.setImageResource(R.drawable.sek)
        "SGD" -> this.setImageResource(R.drawable.sgd)
        "THB" -> this.setImageResource(R.drawable.thb)
        "TRY" -> this.setImageResource(R.drawable.tryy)
        "USD" -> this.setImageResource(R.drawable.usd)
        "ZAR" -> this.setImageResource(R.drawable.zar)
        "transparent" -> this.setImageResource(R.drawable.transparent)
    }
}

@SuppressLint("SetTextI18n")
fun TextView.addText(text: String, size: Int) {
    if (size > 1)
        this.text = this.text.toString() + text
    else
        (this.context as MainActivity).snacky("Please Select at least 2 currency from Settings", true, "Select")
}

fun AdView.loadAd(tag: String?) {
    val adId = if (BuildConfig.DEBUG) {
        R.string.banner_ad_unit_id_test
    } else when (tag) {
        MainFragment.TAG -> R.string.banner_ad_unit_id_main
        SettingsFragment.TAG -> R.string.banner_ad_unit_id_settings
        else -> R.string.banner_ad_unit_id_test
    }
    MobileAds.initialize(context, resources.getString(adId))
    val adRequest = AdRequest.Builder().build()
    this.loadAd(adRequest)
}

fun WebView.fadeIO(isIn: Boolean) {
    if (isIn)
        this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    else
        this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
}