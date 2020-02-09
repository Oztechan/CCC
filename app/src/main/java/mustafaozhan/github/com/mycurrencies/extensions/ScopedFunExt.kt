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

fun <T1, T2> T1.mapTo(
    method: T1.(condition: T1) -> T2
) = method(this)
