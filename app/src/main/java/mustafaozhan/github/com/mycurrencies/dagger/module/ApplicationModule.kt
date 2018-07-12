package mustafaozhan.github.com.mycurrencies.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.annotation.ApplicationContext
import mustafaozhan.github.com.mycurrencies.application.Application

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:48 PM on Arch Linux wit Love <3.
 */
@Module
class ApplicationModule(private val application: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return application.applicationContext
    }

}