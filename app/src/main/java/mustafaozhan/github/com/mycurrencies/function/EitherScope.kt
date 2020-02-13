package mustafaozhan.github.com.mycurrencies.function

inline fun <reified T> T.either(
    vararg method: T.(condition: T) -> Boolean
) =
    if (this != null) {
        var temp = false
        method.forEach { temp = temp || it(this) }
        if (temp) {
            this
        } else {
            null
        }
    } else {
        null
    }

inline fun <reified T> T.eitherNot(
    vararg method: T.(condition: T) -> Boolean
) =
    if (this != null) {
        var temp = false
        method.forEach { temp = temp || it(this) }
        if (!temp) {
            this
        } else {
            null
        }
    } else {
        null
    }
