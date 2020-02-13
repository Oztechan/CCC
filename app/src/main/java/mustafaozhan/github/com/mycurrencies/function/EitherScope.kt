package mustafaozhan.github.com.mycurrencies.function

inline fun <reified T> T.either(
    vararg method: T.(condition: T) -> Boolean
) =
    if (this != null) {
        if (method.any { it(this) }) {
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
        if (!method.any { it(this) }) {
            this
        } else {
            null
        }
    } else {
        null
    }
