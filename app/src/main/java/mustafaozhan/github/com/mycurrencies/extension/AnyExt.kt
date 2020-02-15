package mustafaozhan.github.com.mycurrencies.extension

import com.crashlytics.android.Crashlytics

inline fun <reified T> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
        Crashlytics.logException(e)
        null
    }
}

inline fun <reified T> Any.castTo() =
    this as? T
