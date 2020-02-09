@file:Suppress("unused")

package mustafaozhan.github.com.mycurrencies.extensions

fun <T> T.whether(
    method: (condition: T) -> Boolean
) = if (this != null && method(this)) {
    this
} else {
    null
}

fun <T> T.whetherNot(
    method: (condition: T) -> Boolean
) = if (this != null && !method(this)) {
    this
} else {
    null
}

fun <T> T.whetherThis(
    method: T.(condition: T) -> Boolean
) = if (this != null && method(this)) {
    this
} else {
    null
}

fun <T> T.whetherThisNot(
    method: T.(condition: T) -> Boolean
) = if (this != null && !method(this)) {
    this
} else {
    null
}
