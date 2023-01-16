object Modules {
    val android = ":android"
    val ios = ":ios"
    val backend = ":backend"

    val client = ":client"
    val res = ":res"
    val common = ":common"
    val billing = ":billing"
    val ad = ":ad"
    val analytics = ":analytics"
    val config = ":config"
    val test = ":test"
    val provider = ":provider"

    object Submodules {
        val logmob = ":submodule:logmob"
        val scopemob = ":submodule:scopemob"
        val basemob = ":submodule:basemob"
        val parsermob = ":submodule:parsermob"
    }
}

val String.packageName: String
    get() = "${ProjectSettings.PROJECT_ID}${replace(":", ".")}"

val String.frameworkName: String
    get() = split(":").joinToString("") { it.capitalize() }
