package mustafaozhan.github.com.mycurrencies.main.fragment

import android.arch.lifecycle.MutableLiveData
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.SettingDao
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import mustafaozhan.github.com.mycurrencies.tools.getResult
import mustafaozhan.github.com.mycurrencies.tools.insertInitialSettings
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

    @Inject
    lateinit var settingDao: SettingDao

    val ratesLiveData: MutableLiveData<Rates> = MutableLiveData()
    var base: String = dataManager.baseCurrency.toString()

    var currencyList: MutableList<Currency> = ArrayList()
    var input: String = ""
    var output: String = "0.0"

    fun getCurrencies() {
        subscribeService(dataManager.getAllOnBase(base),
                ::eventDownloadSuccess, ::eventDownloadFail)

    }

    private fun eventDownloadSuccess(currencyResponse: CurrencyResponse) {
        currencyResponse.rates
        ratesLiveData.postValue(currencyResponse.rates)

    }

    private fun eventDownloadFail(t: Throwable) {
    }

    fun initData() {
        if (dataManager.firstTime) {
            settingDao.insertInitialSettings()
            dataManager.firstTime = false
        }
        settingDao.getActiveSettings().forEach {
            currencyList.add(Currency(it.name))

        }
        currencyList.forEach { it.rate=getResult(it.name,output,ratesLiveData.value) }
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
}


