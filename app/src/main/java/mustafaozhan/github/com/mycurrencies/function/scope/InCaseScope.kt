package mustafaozhan.github.com.mycurrencies.function.scope

inline fun <reified T> T.inCase(
    condition: Boolean?,
    method: T.(T) -> Unit
) = if (condition == true) {
    method(this)
    this
} else null

inline fun <reified T> T.inCaseNot(
    condition: Boolean?,
    method: T.(T) -> Unit
) = if (condition == false) {
    method(this)
    this
} else null
