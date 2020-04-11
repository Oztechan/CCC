package mustafaozhan.github.com.mycurrencies.ui.main.fragment

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.view.BaseViewState

data class DataViewState(val observer: DataViewStateObserver) : BaseViewState() {
    val base: MutableLiveData<String> = observer.base
}

data class DataViewStateObserver(
    val base: MediatorLiveData<String> = MediatorLiveData<String>()
)
