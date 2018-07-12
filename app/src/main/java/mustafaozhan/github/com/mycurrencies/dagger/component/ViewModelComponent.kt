package mustafaozhan.github.com.mycurrencies.dagger.component

import dagger.Subcomponent
import mustafaozhan.github.com.mycurrencies.annotation.PerViewModel
import mustafaozhan.github.com.mycurrencies.dagger.module.ViewModelModule
import mustafaozhan.github.com.mycurrencies.main.MainActivityViewModel

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:48 PM on Arch Linux wit Love <3.
 */
@PerViewModel
@Subcomponent(modules = [(ViewModelModule::class)])
interface ViewModelComponent {
    fun inject(mainActivityViewModel: MainActivityViewModel)
}