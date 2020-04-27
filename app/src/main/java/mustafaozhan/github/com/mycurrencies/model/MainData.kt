// Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
package mustafaozhan.github.com.mycurrencies.model

import com.squareup.moshi.JsonClass
import org.joda.time.Instant

@JsonClass(generateAdapter = true)
data class MainData(
    var firstRun: Boolean = true,
    var currentBase: Currencies = Currencies.EUR,
    var adFreeActivatedDate: Instant? = null,
    var sliderShown: Boolean? = false
)
