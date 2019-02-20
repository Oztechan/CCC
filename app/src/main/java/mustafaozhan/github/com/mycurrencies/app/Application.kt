package mustafaozhan.github.com.mycurrencies.app

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.google.firebase.analytics.FirebaseAnalytics
import mustafaozhan.github.com.mycurrencies.dagger.component.ApplicationComponent
import mustafaozhan.github.com.mycurrencies.dagger.component.DaggerApplicationComponent
import mustafaozhan.github.com.mycurrencies.dagger.module.ApplicationModule

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:43 PM on Arch Linux wit Love <3.
 */
class Application : MultiDexApplication() {
    companion object {
        lateinit var instance: Application

        fun get(context: Context): Application {
            return context.applicationContext as Application
        }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseAnalytics.getInstance(this)
        instance = this
    }

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder() // will be auto generated after build
            .applicationModule(ApplicationModule(this)).build()
    }
}