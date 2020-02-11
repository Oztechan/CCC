package mustafaozhan.github.com.mycurrencies.extensions

fun <T> T.whether(
    method: T.(condition: T) -> Boolean
) =
    if (this != null && method(this)) {
        this
    } else {
        null
    }

fun <T> T.unless(
    method: T.(condition: T) -> Boolean
) =
    if (this != null && !method(this)) {
        this
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
