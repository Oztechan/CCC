package mustafaozhan.github.com.mycurrencies.function.lambda

inline fun <reified T, R> ensure(
    vararg elements: T?,
    closureSafe: () -> R
) =
    if (elements.all { it != null }) {
        closureSafe()
    } else {
        null
    }

inline fun <reified T> T?.inCase(block: () -> Unit) {
    if (this == null) block()
}
