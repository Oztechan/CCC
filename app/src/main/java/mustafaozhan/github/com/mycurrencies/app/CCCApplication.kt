package mustafaozhan.github.com.mycurrencies.app

import androidx.multidex.MultiDexApplication
import com.github.mustafaozhan.logmob.initLogMob
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import mustafaozhan.github.com.mycurrencies.di.DaggerAppComponent
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:43 PM on Arch Linux wit Love <3.
 */
class CCCApplication : MultiDexApplication(), HasAndroidInjector {
    companion object {
        lateinit var instance: CCCApplication
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        instance = this

        initLogMob(this, true)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
