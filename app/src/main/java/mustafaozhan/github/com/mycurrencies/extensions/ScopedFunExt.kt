package mustafaozhan.github.com.mycurrencies.extensions

fun <T> T.whether(
    method: T.(condition: T) -> Boolean
) =
    if (this != null && method(this)) {
        this
    } else {
        null
    }

fun <T> T.whether(
    vararg method: T.(condition: T) -> Boolean
) =
    if (this != null) {
        if (method.all { it(this) }) {
            this
        } else {
            null
        }
    } else {
        null
    }

fun <T> T.either(
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

fun <T> T.whetherNot(
    method: T.(condition: T) -> Boolean
) =
    if (this != null && !method(this)) {
        this
    } else {
        null
    }

fun <T> T.whetherNot(
    vararg method: T.(condition: T) -> Boolean
) =
    if (this != null) {
        if (!method.all { it(this) }) {
            this
        } else {
            null
        }
    } else {
        null
    }

fun <T> T.eitherNot(
    vararg method: T.(condition: T) -> Boolean
) =
    if (this != null) {
        if (method.any { !it(this) }) {
            this
        } else {
            null
        }
    } else {
        null
    }

fun <T, R> T.mapTo(
    transform: T.(map: T) -> R?
): R? =
    transform(this)

@Suppress("UNCHECKED_CAST")
fun <T, R> T?.castTo(
    cast: T.() -> Class<R>
): R? =
    if (this != null) {
        if (cast(this).isInstance(this)) {
            this as? R
        } else {
            null
        }
    } else {
        null
    }
