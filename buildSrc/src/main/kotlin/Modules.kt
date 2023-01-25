object Modules {

    object Android {
        const val app = ":android:app"

        object Feature {
            const val mobile = ":android:feature:mobile"
            const val widget = ":android:feature:widget"
        }

        object Core {
            const val billing = ":android:core:billing"
            const val ad = ":android:core:ad"
        }
    }

    @Suppress("unused")
    object IOS {
        const val self = ":ios" // not used just for presentation
    }

    object Backend {
        const val self = ":backend"

        object Service {
            const val free = ":backend:service:free"
            const val premium = ":backend:service:premium"
        }
    }

    object Common {
        object Core {
            const val database = ":common:core:database"
            const val network = ":common:core:network"
            const val infrastructure = ":common:core:infrastructure"
            const val model = ":common:core:model"
        }

        object DataSource {
            const val conversion = ":common:datasource:conversion"
        }
    }

    object Client {
        const val self = ":client"

        object Core {
            const val persistence = ":client:core:persistence"
            const val remoteConfig = ":client:core:remoteconfig"
            const val infrastructure = ":client:core:infrastructure"
        }

        object Storage {
            const val app = ":client:storage:app"
            const val calculator = ":client:storage:calculator"
        }

        object DataSource {
            const val currency = ":client:datasource:currency"
            const val watcher = ":client:datasource:watcher"
        }

        object Service {
            const val backend = ":client:service:backend"
        }

        object ConfigService {
            const val ad = ":client:configservice:ad"
            const val review = ":client:configservice:review"
            const val update = ":client:configservice:update"
        }
    }

    const val res = ":res"
    const val analytics = ":analytics"
    const val config = ":config"
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
