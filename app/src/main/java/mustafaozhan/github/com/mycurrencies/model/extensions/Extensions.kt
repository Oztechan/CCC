package mustafaozhan.github.com.mycurrencies.model.extensions

import android.widget.ImageView
import mustafaozhan.github.com.mycurrencies.R

/**
 * Created by Mustafa Ozhan on 10/29/17 at 11:22 AM on Arch Linux.
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
        "transparent"->this.setImageResource(R.drawable.transparent)
    }
}