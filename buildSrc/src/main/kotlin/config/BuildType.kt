package config

import java.util.Locale

enum class BuildType {
    DEBUG,
    RELEASE;

    companion object {
        val debug = DEBUG.name.toLowerCase(Locale.ROOT)
        val release = RELEASE.name.toLowerCase(Locale.ROOT)
    }
}
