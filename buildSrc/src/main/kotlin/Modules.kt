object Modules {
    const val CLIENT = ":client"
    const val RES = ":res"
    const val COMMON = ":common"
    const val BILLING = ":billing"
    const val AD = ":ad"
    const val ANALYTICS = ":analytics"
    const val CONFIG = ":config"
    const val TEST = ":test"
    const val PROVIDER = ":provider"

    // sub modules
    const val LOGMOB = ":logmob"
    const val SCOPEMOB = ":scopemob"
    const val BASEMOB = ":basemob"
    const val PARSERMOB = ":parsermob"

    // targets
    const val ANDROID = ":android"
    const val IOS = ":ios"
    const val BACKEND = ":backend"
}

val String.packageName: String
    get() = "${ProjectSettings.PROJECT_ID}${replace(":", ".")}"

val String.frameworkName: String
    get() = split(":").joinToString("") { it.capitalize() }
