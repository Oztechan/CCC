package mustafaozhan.github.com.mycurrencies.function.scope

inline fun <reified T> T.inCase(
    condition: Boolean?,
    method: T.(T) -> Unit
): T {
    if (condition == true) {
        method(this)
    }
    return this
}

inline fun <reified T> T.inCaseNot(
    condition: Boolean?,
    method: T.(T) -> Unit
): T {
    if (condition == false) {
        method(this)
    }
    return this
}
