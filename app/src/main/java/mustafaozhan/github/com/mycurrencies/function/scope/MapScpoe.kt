package mustafaozhan.github.com.mycurrencies.function.scope

inline fun <reified T, reified R> T.mapTo(
    transform: T.(map: T) -> R?
): R? =
    transform(this)
