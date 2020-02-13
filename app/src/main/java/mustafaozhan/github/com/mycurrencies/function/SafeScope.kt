package mustafaozhan.github.com.mycurrencies.function

inline fun <reified T> ifAllSafe(
    vararg elements: T?,
    closureSafe: () -> Unit
) =
    if (elements.all { it != null }) {
        closureSafe()
    } else {
        null
    }
