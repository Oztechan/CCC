package mustafaozhan.github.com.mycurrencies.base.model

import mustafaozhan.github.com.mycurrencies.tools.Currencies

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
data class MainData(
        var firstRun: Boolean,
        var firstCache:Boolean,
        var baseCurrency: Currencies,
        var currentBase: Currencies
)