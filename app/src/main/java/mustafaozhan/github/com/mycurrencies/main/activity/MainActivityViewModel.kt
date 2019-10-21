package mustafaozhan.github.com.mycurrencies.main.activity

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.tools.DataManager
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
class MainActivityViewModel(
    override var dataManager: DataManager
) : BaseViewModel() {

    override fun onLoaded(): Completable {
        return Completable.complete()
    }

    fun updateAdFreeActivation() {
        val mainData = dataManager.loadMainData()
        mainData.adFreeActivatedDate = Instant.now()
        dataManager.persistMainData(mainData)
    }
}
