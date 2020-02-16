package mustafaozhan.github.com.mycurrencies.function.lambda

inline fun <reified T> assurance(
    vararg elements: T?,
    assurance: () -> Unit
) =
    if (elements.all { it != null }) {
        assurance()
    } else {
        null
    }
