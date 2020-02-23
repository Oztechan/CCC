package mustafaozhan.github.com.mycurrencies.function.extension

import timber.log.Timber

inline fun <reified T> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        Timber.e(e)
        null
    }
}

inline fun <reified T> Any.castTo() =
    this as? T
