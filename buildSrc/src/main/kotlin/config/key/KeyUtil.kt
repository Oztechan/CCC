package config.key

import config.BuildType
import getSecret
import org.gradle.api.Project

val String.resId: String
    get() = lowercase()

val Project.string: String
    get() = "string"

fun Project.secret(key: Key) = getSecret(
    key = key.name,
    default = key.default
)

fun Project.secret(
    key: TypedKey,
    buildType: BuildType
) = getSecret(
    key = "${buildType.name}_${key.name}",
    default = key.default
)
