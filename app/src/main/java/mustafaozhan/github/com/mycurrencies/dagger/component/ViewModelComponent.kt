package mustafaozhan.github.com.mycurrencies.dagger.component

import dagger.Subcomponent
import mustafaozhan.github.com.mycurrencies.annotation.PerViewModel
import mustafaozhan.github.com.mycurrencies.dagger.module.ViewModelModule
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivityViewModel
import mustafaozhan.github.com.mycurrencies.main.fragment.MainFragmentViewModel
import mustafaozhan.github.com.mycurrencies.settings.SettingsFragmentViewModel

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:48 PM on Arch Linux wit Love <3.
 */
@PerViewModel
@Subcomponent(modules = [(ViewModelModule::class)])
interface ViewModelComponent {
    fun inject(mainActivityViewModel: MainActivityViewModel)
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}