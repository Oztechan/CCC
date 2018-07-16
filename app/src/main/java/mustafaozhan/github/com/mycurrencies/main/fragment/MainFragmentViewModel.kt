package mustafaozhan.github.com.mycurrencies.main.fragment

import android.arch.lifecycle.MutableLiveData
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates
import mustafaozhan.github.com.mycurrencies.tools.Currencies

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class MainFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    val ratesLiveData: MutableLiveData<Rates> = MutableLiveData()
    var currencyList: MutableList<Currency> = ArrayList()

    fun getCurrencies() {
        val asd: ArrayList<Currencies>? = ArrayList()
        asd?.add(Currencies.USD)
        asd?.add(Currencies.CZK)
        asd?.add(Currencies.TRY)
        asd?.add(Currencies.ZAR)
        asd?.add(Currencies.GBP)
        asd?.let {
            dataManager.getAllOnBaseAndLimithWith(Currencies.EUR, it)
        }?.let {
            subscribeService(it, ::eventDownloadSuccess, ::eventDownloadFail)
        }
    }

    private fun eventDownloadSuccess(currencyResponse: CurrencyResponse) {
        currencyResponse.rates
        ratesLiveData.postValue(currencyResponse.rates)

    }

    private fun eventDownloadFail(t: Throwable) {
    }
}