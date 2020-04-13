package mustafaozhan.github.com.mycurrencies.ui.main.fragment

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseState

data class DataState(val observer: DataViewStateObserver) : BaseState() {
    val base: MutableLiveData<String> = observer.base
}

data class DataViewStateObserver(
    val base: MediatorLiveData<String> = MediatorLiveData<String>()
)
