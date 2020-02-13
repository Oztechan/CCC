package mustafaozhan.github.com.mycurrencies.function

inline fun <reified T, reified R> T.mapTo(
    transform: T.(map: T) -> R?
): R? =
    transform(this)

inline fun <reified T, reified R> T.castTo() =
    if (this != null) {
        this as? R
    } else {
        null
    }
