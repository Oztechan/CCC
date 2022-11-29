package config

import org.gradle.configurationcache.extensions.capitalized

data class Module(
    val name: String,
    val path: String = ":$name",
    val frameworkName: String = name.capitalized()
)
