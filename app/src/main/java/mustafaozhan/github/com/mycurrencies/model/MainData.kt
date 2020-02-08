package mustafaozhan.github.com.mycurrencies.model

import com.squareup.moshi.JsonClass
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
@JsonClass(generateAdapter = true)
data class MainData(
    var firstRun: Boolean = true,
    var currentBase: Currencies = Currencies.EUR,
    var adFreeActivatedDate: Instant? = null,
    var sliderShown: Boolean? = false
)
