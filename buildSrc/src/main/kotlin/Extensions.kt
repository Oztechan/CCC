import java.io.IOException
import java.util.Locale
import java.util.Properties
import org.gradle.api.Project

private const val PATH_SECRET_PROPERTIES = "../secret.properties"

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

private fun Project.getSecretProperties() = try {
    Properties().apply { load(file(PATH_SECRET_PROPERTIES).inputStream()) }
} catch (e: IOException) {
    logger.debug(e.message, e)
    null
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf(
        "RELEASE",
        "FINAL",
        "GA"
    ).any {
        this.toUpperCase(Locale.ROOT).contains(it)
    }

    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}
