package config

import java.util.Locale

enum class DeviceFlavour {
    GOOGLE,
    HUAWEI;

    @Suppress("unused")
    companion object {
        val google = GOOGLE.name.toLowerCase(Locale.ROOT)
        val huawei = HUAWEI.name.toLowerCase(Locale.ROOT)

        val flavorDimension = DeviceFlavour::class.simpleName.toString()

        val googleImplementation = "${google}Implementation"
        val huaweiImplementation = "${huawei}iImplementation"
        val googleApi = "${google}Api"
        val huaweiApi = "${huawei}Api"
    }
}
