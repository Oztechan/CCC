package mustafaozhan.github.com.mycurrencies.main

import mustafaozhan.github.com.mycurrencies.base.BaseViewModel

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:50 PM on Arch Linux wit Love <3.
 */
class MainActivityViewModel : BaseViewModel() {
    override fun inject() {
        viewModelComponent.inject(this)
    }
}