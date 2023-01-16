object Modules {
    const val android = ":android"
    const val ios = ":ios"
    const val backend = ":backend"

    const val client = ":client"
    const val res = ":res"
    const val common = ":common"
    const val billing = ":billing"
    const val ad = ":ad"
    const val analytics = ":analytics"
    const val config = ":config"
    const val test = ":test"
    const val provider = ":provider"

    object Submodules {
        const val logmob = ":submodule:logmob"
        const val scopemob = ":submodule:scopemob"
        const val basemob = ":submodule:basemob"
        const val parsermob = ":submodule:parsermob"
    }
}

val String.packageName: String
    get() = "${ProjectSettings.PROJECT_ID}${replace(":", ".")}"

val String.frameworkName: String
    get() = split(":").joinToString("") { it.capitalize() }
