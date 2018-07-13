package mustafaozhan.github.com.mycurrencies.main.fragment

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class MainFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    val eventsLiveData: MutableLiveData<MutableList<Rates>> = MutableLiveData()
    //    val errorLiveData: MutableLiveData<LoadingViewState> = MutableLiveData()
    fun getCurrencies() {
        subscribeService(dataManager.getAllCurrencies(), ::eventDownloadSuccess, ::eventDownloadFail)
    }


    private fun eventDownloadSuccess(eventsResponse: CurrencyResponse) {
        eventsResponse.rates

        Log.d("dasasd", "dasasd")
//        eventsLiveData.postValue(eventsResponse.rates?.toMutableList())
    }

    private fun eventDownloadFail(t: Throwable) {
        Log.d("dasasd", "dasasd")
//        errorLiveData.postValue(StateError(R.string.events_download_fail, ::downloadEvents))
    }
}