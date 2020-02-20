package mustafaozhan.github.com.mycurrencies.tool

inline fun <reified T : Enum<T>> enumValueOrNull(name: String): T? {
    return enumValues<T>().find { it.name == name }
}
