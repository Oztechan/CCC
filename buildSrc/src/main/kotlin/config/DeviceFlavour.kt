package config

enum class DeviceFlavour {
    GOOGLE,
    HUAWEI;

    @Suppress("unused")
    companion object {
        val google = GOOGLE.name.lowercase()
        val huawei = HUAWEI.name.lowercase()

        val flavorDimension = DeviceFlavour::class.simpleName.toString()

        val DeviceFlavour.implementation: String
            get() = "${name.lowercase()}Implementation"
    }
}
