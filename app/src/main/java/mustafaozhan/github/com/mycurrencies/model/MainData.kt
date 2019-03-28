package mustafaozhan.github.com.mycurrencies.model

import mustafaozhan.github.com.mycurrencies.tools.Currencies

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
data class MainData(
    var initialRun: Boolean,
    var currentBase: Currencies
)