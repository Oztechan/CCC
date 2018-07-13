package mustafaozhan.github.com.mycurrencies.tools

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.base.api.exchangerates.ExchangeRatesApiHelper
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
@Singleton
class DataManager @Inject
constructor(private val generalSharedPreferences: GeneralSharedPreferences) {

    @Inject
    lateinit var exchangeRatesApiHelper: ExchangeRatesApiHelper

    fun getAllCurrencies(): Observable<CurrencyResponse> = exchangeRatesApiHelper.exchangeRatesApiServices.getAllCurrencies()
}