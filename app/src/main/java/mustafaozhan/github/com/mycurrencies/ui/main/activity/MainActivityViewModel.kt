package mustafaozhan.github.com.mycurrencies.ui.main.activity

import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
class MainActivityViewModel(
    override var preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    override fun onLoaded(): Completable {
        return Completable.complete()
    }

    fun updateAdFreeActivation() {
        val mainData = preferencesRepository.loadMainData()
        mainData.adFreeActivatedDate = Instant.now()
        preferencesRepository.persistMainData(mainData)
    }
}
