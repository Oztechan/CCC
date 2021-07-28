import org.gradle.api.Project
import java.io.IOException
import java.util.Properties

private const val PATH_SECRET_PROPERTIES = "../secret.properties"

fun Project.getSecretProperties() = try {
    Properties().apply { load(file(PATH_SECRET_PROPERTIES).inputStream()) }
} catch (e: IOException) {
    logger.debug(e.message, e)
    null
}

fun Project.getSecret(
    key: String,
    default: String = "secret" // these values can not be public
): String = System.getenv(key).let {
    if (it.isNullOrEmpty()) {
        getSecretProperties()?.get(key)?.toString() ?: default
    } else {
        it
    }
}

fun String.removeVariant() = replace(
    oldValue = "_${BuildType.RELEASE}_",
    newValue = "_",
    ignoreCase = true
).replace(
    oldValue = "_${BuildType.DEBUG}_",
    newValue = "_",
    ignoreCase = true
)
