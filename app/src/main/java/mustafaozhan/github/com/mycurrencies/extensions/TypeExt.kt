@file:Suppress("unused")

package mustafaozhan.github.com.mycurrencies.extensions

fun <T : Any?> T?.whether(
    method: (condition: T) -> Boolean
) = if (this != null && method(this)) {
    this
} else {
    null
}

fun <T : Any?> T?.whetherNot(
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
