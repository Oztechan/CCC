import org.gradle.api.Project
import java.io.IOException
import java.util.Properties

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
    Properties().apply {
        load(
            file("${rootProject.rootDir}/secret.properties").inputStream()
        )
    }
} catch (e: IOException) {
    logger.debug(e.message, e)
    null
}
