import org.gradle.configurationcache.extensions.capitalized

object Modules {

    object Android {
        const val app = ":android:app"

        object UI {
            const val mobile = ":android:ui:mobile"
            const val widget = ":android:ui:widget"
        }

        object Core {
            const val billing = ":android:core:billing"
            const val ad = ":android:core:ad"
        }

        object ViewModel {
            const val premium = ":android:viewmodel:premium"
            const val widget = ":android:viewmodel:widget"
        }
    }

    object IOS {
        const val provider = ":ios:provider"

        object Repository {
            const val background = ":ios:repository:background"
        }
    }

    object Backend {
        const val app = ":backend:app"

        object Service {
            const val free = ":backend:service:free"
            const val premium = ":backend:service:premium"
        }

        object Controller {
            const val sync = ":backend:controller:sync"
            const val api = ":backend:controller:api"
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
        object Core {
            const val viewModel = ":client:core:viewmodel"
            const val persistence = ":client:core:persistence"
            const val remoteConfig = ":client:core:remoteconfig"
            const val analytics = ":client:core:analytics"
            const val res = ":client:core:res"
            const val shared = ":client:core:shared"
        }

        object Storage {
            const val app = ":client:storage:app"
            const val calculation = ":client:storage:calculation"
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

        object Repository {
            const val adControl = ":client:repository:adcontrol"
            const val appConfig = ":client:repository:appconfig"
        }

        object ViewModel {
            const val main = ":client:viewmodel:main"
            const val calculator = ":client:viewmodel:calculator"
            const val currencies = ":client:viewmodel:currencies"
            const val settings = ":client:viewmodel:settings"
            const val selectCurrency = ":client:viewmodel:selectcurrency"
            const val watchers = ":client:viewmodel:watchers"
        }
    }

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
    get() = split(":").lastOrNull()?.capitalized().orEmpty()
