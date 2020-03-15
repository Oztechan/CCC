package mustafaozhan.github.com.mycurrencies.extension

import mustafaozhan.github.com.logmob.logError

inline fun <reified T> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        logError(e)
        null
    }
}

inline fun <reified T> Any.castTo() =
    this as? T
