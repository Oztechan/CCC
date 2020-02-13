package mustafaozhan.github.com.mycurrencies.function

inline fun <reified T> T.whether(
    method: T.(condition: T) -> Boolean
) =
    if (this != null && method(this)) {
        this
    } else {
        null
    }

inline fun <reified T> T.whether(
    vararg method: T.(condition: T) -> Boolean
) =
    if (this != null) {
        var temp = true
        method.forEach { temp = temp && it(this) }
        if (temp) {
            this
        } else {
            null
        }
    } else {
        null
    }

inline fun <reified T> T.whetherNot(
    method: T.(condition: T) -> Boolean
) =
    if (this != null && !method(this)) {
        this
    } else {
        null
    }

inline fun <reified T> T.whetherNot(
    vararg method: T.(condition: T) -> Boolean
) =
    if (this != null) {
        var temp = true
        method.forEach { temp = temp && it(this) }
        if (!temp) {
            this
        } else {
            null
        }
    } else {
        null
    }
