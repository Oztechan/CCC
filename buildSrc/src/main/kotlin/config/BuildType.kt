package config

import java.util.Locale

enum class BuildType {
    DEBUG,
    RELEASE;

    companion object {
        val debug = DEBUG.name.lowercase()
        val release = RELEASE.name.lowercase()
    }
}
