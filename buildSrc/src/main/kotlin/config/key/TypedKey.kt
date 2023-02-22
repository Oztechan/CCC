package config.key

enum class TypedKey(
    val default: String = ""
) {
    ADVERTISEMENT_ID_GOOGLE(Fakes.Google.ADVERTISEMENT_ID),
    ADVERTISEMENT_ID_HUAWEI(Fakes.Huawei.ADVERTISEMENT_ID)
}
