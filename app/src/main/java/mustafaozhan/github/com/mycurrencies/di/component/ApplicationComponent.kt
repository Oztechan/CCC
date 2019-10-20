package mustafaozhan.github.com.mycurrencies.di.component

import android.content.Context
import dagger.Component
import mustafaozhan.github.com.mycurrencies.annotation.ApplicationContext
import mustafaozhan.github.com.mycurrencies.di.module.ApplicationModule
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:47 PM on Arch Linux wit Love <3.
 */
@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {
    @ApplicationContext
    fun context(): Context

    fun viewModelComponent(): ViewModelComponent
}
