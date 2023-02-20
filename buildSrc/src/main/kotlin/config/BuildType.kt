package config

enum class BuildType {
    DEBUG,
    RELEASE;

    companion object {
        val debug = DEBUG.name.lowercase()
        val release = RELEASE.name.lowercase()
    }
}
