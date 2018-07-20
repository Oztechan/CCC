package mustafaozhan.github.com.mycurrencies.tools

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.base.api.exchangerates.ExchangeRatesApiHelper
import mustafaozhan.github.com.mycurrencies.base.model.MainData
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.room.model.Currency
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

    var firstTime: Boolean = true
    var baseCurrency: Currencies? = Currencies.EUR
    var currentBase = Currencies.EUR

    fun getAllOnBase(base: Currencies): Observable<CurrencyResponse> =
            exchangeRatesApiHelper.exchangeRatesApiServices.getAllOnBase(base)

    fun getAllOnBaseAndLimithWith(base: String, limit: ArrayList<Currencies>): Observable<CurrencyResponse> =
            exchangeRatesApiHelper.exchangeRatesApiServices.getAllOnBaseAndLimitWith(base, limit)

    fun loadMainData() = generalSharedPreferences.loadMainData()

    fun persistMainData(mainData: MainData){
        generalSharedPreferences.persistMainData(mainData)
    }

}