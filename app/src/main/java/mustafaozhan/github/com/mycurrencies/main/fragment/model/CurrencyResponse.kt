package mustafaozhan.github.com.mycurrencies.main.fragment.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mustafa Ozhan on 2018-07-13.
 */
data class CurrencyResponse(
        @SerializedName("base") var base: String? = null,
        @SerializedName("date") var date: String? = null,
        @SerializedName("rates") var rates: Rates? = null
)

data class Rates(
        @SerializedName("eur") var eUR: Double? = null,
        @SerializedName("aud") var aUD: Double? = null,
        @SerializedName("bgn") var bGN: Double? = null,
        @SerializedName("brl") var bRL: Double? = null,
        @SerializedName("cad") var cAD: Double? = null,
        @SerializedName("chf") var cHF: Double? = null,
        @SerializedName("cny") var cNY: Double? = null,
        @SerializedName("czk") var cZK: Double? = null,
        @SerializedName("dkk") var dKK: Double? = null,
        @SerializedName("gbp") var gBP: Double? = null,
        @SerializedName("hkd") var hKD: Double? = null,
        @SerializedName("hrk") var hRK: Double? = null,
        @SerializedName("huf") var hUF: Double? = null,
        @SerializedName("idr") var iDR: Double? = null,
        @SerializedName("ils") var iLS: Double? = null,
        @SerializedName("inr") var iNR: Double? = null,
        @SerializedName("jpy") var jPY: Double? = null,
        @SerializedName("krw") var kRW: Double? = null,
        @SerializedName("mxn") var mXN: Double? = null,
        @SerializedName("myr") var mYR: Double? = null,
        @SerializedName("nok") var nOK: Double? = null,
        @SerializedName("nzd") var nZD: Double? = null,
        @SerializedName("php") var pHP: Double? = null,
        @SerializedName("pln") var pLN: Double? = null,
        @SerializedName("ron") var rON: Double? = null,
        @SerializedName("rub") var rUB: Double? = null,
        @SerializedName("sek") var sEK: Double? = null,
        @SerializedName("sgd") var sGD: Double? = null,
        @SerializedName("thb") var tHB: Double? = null,
        @SerializedName("try") var tRY: Double? = null,
        @SerializedName("usd") var uSD: Double? = null,
        @SerializedName("zar") var zAR: Double? = null
)