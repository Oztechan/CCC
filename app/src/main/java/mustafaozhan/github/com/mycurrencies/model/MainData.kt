package mustafaozhan.github.com.mycurrencies.model

import mustafaozhan.github.com.mycurrencies.tools.Currencies
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
data class MainData(
    var firstRun: Boolean,
    var currentBase: Currencies,
    var adFreeActivatedDate: Instant?
)