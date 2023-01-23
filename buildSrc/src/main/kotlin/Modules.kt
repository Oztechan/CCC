object Modules {

    object Android {
        const val app = ":android:app"

        object Feature {
            const val mobile = ":android:feature:mobile"
            const val widget = ":android:feature:widget"
        }
    }

    const val ios = ":ios"
    const val backend = ":backend"

    const val common = ":common"

    object Common {
        object Core {
            const val database = ":common:core:database"
            const val network = ":common:core:network"
            const val infrastructure = ":common:core:infrastructure"
            const val model = ":common:core:model"
        }

        object Service {
            const val free = ":common:service:free"
            const val premium = ":common:service:premium"
            const val backend = ":common:service:backend"
        }

        object Datasource {
            const val currency = ":common:datasource:currency"
            const val watcher = ":common:datasource:watcher"
            const val conversion = ":common:datasource:conversion"
        }
    }

    const val client = ":client"
    const val res = ":res"
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
