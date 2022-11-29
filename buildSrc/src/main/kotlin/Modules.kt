import org.gradle.configurationcache.extensions.capitalized

class Modules(
    val name: String,
    val path: String = ":$name",
    val frameworkName: String = name.capitalized(),
    val packageName: String = "${ProjectSettings.PROJECT_ID}.${name}"
) {
    companion object {
        val CLIENT = Modules("client")
        val RES = Modules("res")
        val COMMON = Modules("common")
        val BILLING = Modules("billing")
        val AD = Modules("ad")
        val ANALYTICS = Modules("analytics")
        val CONFIG = Modules("config")
        val TEST = Modules("test")
        val PROVIDER = Modules("provider")

        // subModuless
        val LOGMOB = Modules("logmob")
        val SCOPEMOB = Modules("scopemob")
        val BASEMOB = Modules("basemob")
        val PARSERMOB = Modules("parsermob")

        // targets
        val ANDROID = Modules("android")
        val IOS = Modules("ios")
        val BACKEND = Modules("backend")
    }
}