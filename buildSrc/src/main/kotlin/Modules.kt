object Modules {
    val android = Module(":android")
    val ios = Module(":ios")
    val backend = Module(":backend")

    val client = Module(":client")
    val res = Module(":res")
    val common = Module(":common")
    val billing = Module(":billing")
    val ad = Module(":ad")
    val analytics = Module(":analytics")
    val config = Module(":config")
    val test = Module(":test")
    val provider = Module(":provider")

    object Submodules {
        val logmob = Module(":submodule:logmob")
        val scopemob = Module(":submodule:scopemob")
        val basemob = Module(":submodule:basemob")
        val parsermob = Module(":submodule:parsermob")
    }

    class Module(val path: String) {
        val packageName = "${ProjectSettings.PROJECT_ID}${path.replace(":", ".")}"
        val frameworkName = path.split(":").joinToString("") { it.capitalize() }
    }
}
