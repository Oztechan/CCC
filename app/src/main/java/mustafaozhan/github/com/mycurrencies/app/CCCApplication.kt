package mustafaozhan.github.com.mycurrencies.app

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import io.fabric.sdk.android.Fabric
import mustafaozhan.github.com.mycurrencies.BuildConfig
import mustafaozhan.github.com.mycurrencies.di.DaggerAppComponent
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:43 PM on Arch Linux wit Love <3.
 */
class CCCApplication : MultiDexApplication(), HasActivityInjector, HasSupportFragmentInjector {
    companion object {
        lateinit var instance: CCCApplication
    }

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        instance = this

        if (!BuildConfig.DEBUG) {
            FirebaseAnalytics.getInstance(this)
        }

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())
    }

    override fun activityInjector(): AndroidInjector<Activity> =
        activityDispatchingAndroidInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> =
        fragmentDispatchingAndroidInjector
}
