package mustafaozhan.github.com.mycurrencies.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mustafa Ozhan on 2018-07-13.
 */
data class CurrencyResponse(
    @SerializedName("base") var base: String,
    @SerializedName("date") var date: String? = null,
    @SerializedName("rates") var rates: Rates
)