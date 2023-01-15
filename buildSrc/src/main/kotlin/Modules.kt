val projectModules = Modules()

class Modules {
    val client = Module("client")
    val res = Module("res")
    val common = Module("common")
    val billing = Module("billing")
    val ad = Module("ad")
    val analytics = Module("analytics")
    val config = Module("config")
    val test = Module("test")
    val provider = Module("provider")

    // sub modules
    val logmob = Module("logmob")
    val scopemob = Module("scopemob")
    val basemob = Module("basemob")
    val parsermob = Module("parsermob")

    // targets
    val android = Module("android")
    val ios = Module("ios")
    val backend = Module("backend")
}

class Module(name: String) {
    val path = ":$name"
    val packageName = "${ProjectSettings.PROJECT_ID}${path.replace(":", ".")}"
    val frameworkName = path.split(":").joinToString("") { it.capitalize() }
}