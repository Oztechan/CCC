package mustafaozhan.github.com.mycurrencies.main.fragment

import android.arch.lifecycle.MutableLiveData
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import mustafaozhan.github.com.mycurrencies.tools.insertInitialCurrencies
import org.mariuszgromada.math.mxparser.Expression
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class MainFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var currencyDao: CurrencyDao

    val ratesLiveData: MutableLiveData<Rates> = MutableLiveData()
    var currencyList: MutableList<Currency> =  mutableListOf()
    var input: String = ""
    var output: String = "0.0"

    fun getCurrencies() {
        subscribeService(dataManager.getAllOnBase(dataManager.currentBase),
                ::eventDownloadSuccess, ::eventDownloadFail)

    }

    private fun eventDownloadSuccess(currencyResponse: CurrencyResponse) {
        currencyResponse.rates
        ratesLiveData.postValue(currencyResponse.rates)

    }

    private fun eventDownloadFail(t: Throwable) {
        t.printStackTrace()
    }

    fun initData() {
        currencyList.clear()
        if (dataManager.firstTime) {
            currencyDao.insertInitialCurrencies()
            dataManager.firstTime = false
        }
        currencyDao.getActiveCurrencies().forEach {
            currencyList.add(Currency(it.name))
        }
    }

    fun calculate(text: String?): String {
        var result: String? = null

        if (text != null) {
            result = if (text.contains("%"))
                Expression(text.replace("%", "/100*")).calculate().toString()
            else
                Expression(text).calculate().toString()
        }

        return result.toString()
    }

    fun getBaseCurrency() = dataManager.baseCurrency
    fun setBaseCurrency(newBase: String) {
        dataManager.baseCurrency = Currencies.valueOf(newBase)
    }

    fun setCurrentBase(newBase: String) {
        currencyList.filter {
            it.name == getCurrentBase().toString()
        }.forEach {
            it.isActive = 1
        }

        dataManager.currentBase = Currencies.valueOf(newBase)
        currencyList.filter {
            it.name == newBase
        }.forEach {
            it.isActive = 0
        }
    }

    fun getCurrentBase() = dataManager.currentBase
    fun checkList() {
        currencyList.filter {
            it.name == getCurrentBase().toString()
        }.forEach {
            it.isActive = 0
        }
    }

}


