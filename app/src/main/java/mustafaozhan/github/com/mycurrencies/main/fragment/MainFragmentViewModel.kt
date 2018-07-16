package mustafaozhan.github.com.mycurrencies.main.fragment

import android.arch.lifecycle.MutableLiveData
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
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

    val eventsLiveData: MutableLiveData<MutableList<Rates>> = MutableLiveData()
    fun getCurrencies() {
        val asd: ArrayList<Currencies>? = ArrayList<Currencies>()
        asd?.add(Currencies.USD)
        asd?.add(Currencies.CZK)
        asd?.let {
            dataManager.getAllOnBaseAndLimithWith(Currencies.EUR, it)
        }?.let {
            subscribeService(it, ::eventDownloadSuccess, ::eventDownloadFail)
        }
    }

    private fun eventDownloadSuccess(eventsResponse: CurrencyResponse) {
        eventsResponse.rates
    }

    private fun eventDownloadFail(t: Throwable) {
    }
}