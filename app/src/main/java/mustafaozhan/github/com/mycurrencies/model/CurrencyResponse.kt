package mustafaozhan.github.com.mycurrencies.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Mustafa Ozhan on 2018-07-13.
 */
@JsonClass(generateAdapter = true)
data class CurrencyResponse(
    @Json(name = "base") var base: String,
    @Json(name = "date") var date: String? = null,
    @Json(name = "rates") var rates: Rates
)
