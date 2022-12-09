import org.gradle.configurationcache.extensions.capitalized

object Modules {
    val CLIENT = "client"
    val RES = "res"
    val COMMON = "common"
    val BILLING = "billing"
    val AD = "ad"
    val ANALYTICS = "analytics"
    val CONFIG = "config"
    val TEST = "test"
    val PROVIDER = "provider"

    // subModuless
    val LOGMOB = "logmob"
    val SCOPEMOB = "scopemob"
    val BASEMOB = "basemob"
    val PARSERMOB = "parsermob"

    // targets
    val ANDROID = "android"
    val IOS = "ios"
    val BACKEND = "backend"

    val String.path: String
        get() = ":$this"

    val String.frameworkName: String
        get() = this.capitalized()

    val String.packageName: String
        get() = "${ProjectSettings.PROJECT_ID}.${this}"
}
