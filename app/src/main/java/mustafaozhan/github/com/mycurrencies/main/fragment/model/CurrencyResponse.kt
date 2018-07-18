package mustafaozhan.github.com.mycurrencies.main.fragment.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mustafa Ozhan on 2018-07-13.
 */
data class CurrencyResponse(
        @SerializedName("baseCurrency") var base: String? = null,
        @SerializedName("date") var date: String? = null,
        @SerializedName("rates") var rates: Rates? = null
)

data class Rates(
        @SerializedName("AUD") var aUD: Double? = null,
        @SerializedName("BGN") var bGN: Double? = null,
        @SerializedName("BRL") var bRL: Double? = null,
        @SerializedName("CAD") var cAD: Double? = null,
        @SerializedName("CHF") var cHF: Double? = null,
        @SerializedName("CNY") var cNY: Double? = null,
        @SerializedName("CZK") var cZK: Double? = null,
        @SerializedName("DKK") var dKK: Double? = null,
        @SerializedName("GBP") var gBP: Double? = null,
        @SerializedName("HKD") var hKD: Double? = null,
        @SerializedName("HRK") var hRK: Double? = null,
        @SerializedName("HUF") var hUF: Double? = null,
        @SerializedName("IDR") var iDR: Double? = null,
        @SerializedName("ILS") var iLS: Double? = null,
        @SerializedName("INR") var iNR: Double? = null,
        @SerializedName("JPY") var jPY: Double? = null,
        @SerializedName("KRW") var kRW: Double? = null,
        @SerializedName("MXN") var mXN: Double? = null,
        @SerializedName("MYR") var mYR: Double? = null,
        @SerializedName("NOK") var nOK: Double? = null,
        @SerializedName("NZD") var nZD: Double? = null,
        @SerializedName("PHP") var pHP: Double? = null,
        @SerializedName("PLN") var pLN: Double? = null,
        @SerializedName("RON") var rON: Double? = null,
        @SerializedName("RUB") var rUB: Double? = null,
        @SerializedName("SEK") var sEK: Double? = null,
        @SerializedName("SGD") var sGD: Double? = null,
        @SerializedName("THB") var tHB: Double? = null,
        @SerializedName("TRY") var tRY: Double? = null,
        @SerializedName("USD") var uSD: Double? = null,
        @SerializedName("ZAR") var zAR: Double? = null,
        @SerializedName("EUR") var eUR: Double? = null
)