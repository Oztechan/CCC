package mustafaozhan.github.com.mycurrencies.model

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
data class MainData(
    var firstRun: Boolean,
    var currentBase: Currencies,
    var sliderShown: Boolean? = false
)
