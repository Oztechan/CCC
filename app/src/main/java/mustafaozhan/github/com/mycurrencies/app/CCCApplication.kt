package mustafaozhan.github.com.mycurrencies.app

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.fabric.sdk.android.Fabric
import mustafaozhan.github.com.mycurrencies.BuildConfig
import mustafaozhan.github.com.mycurrencies.di.DaggerAppComponent
import mustafaozhan.github.com.mycurrencies.log.CCCDebugTree
import mustafaozhan.github.com.mycurrencies.log.CrashlyticsTree
import mustafaozhan.github.com.mycurrencies.log.WatchDogHandler
import timber.log.Timber
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

        Fabric.with(
            this,
            Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
        )

        Timber.plant(if (BuildConfig.DEBUG) CCCDebugTree(this) else CrashlyticsTree())

        Thread.setDefaultUncaughtExceptionHandler(WatchDogHandler())
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
