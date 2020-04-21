package mustafaozhan.github.com.mycurrencies.util

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale

fun updateBaseContextLocale(context: Context): Context? {
    val locale = Locale.US
    Locale.setDefault(locale)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        updateResourcesLocale(context, locale)
    } else {
        updateResourcesLocaleLegacy(context, locale)
    }
}

@TargetApi(Build.VERSION_CODES.N)
private fun updateResourcesLocale(context: Context, locale: Locale): Context? {
    val configuration: Configuration = context.resources.configuration
    configuration.setLocale(locale)
    return context.createConfigurationContext(configuration)
}

@Suppress("DEPRECATION")
private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context? {
    val resources: Resources = context.resources
    val configuration: Configuration = resources.configuration
    configuration.locale = locale
    resources.updateConfiguration(configuration, resources.displayMetrics)
    return context
}
