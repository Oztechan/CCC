import org.gradle.configurationcache.extensions.capitalized

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

    object IOS {
        const val provider = ":ios:provider"
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
            const val background = ":client:repository:background"
            const val appConfig = ":client:repository:appconfig"
        }

        object ViewModel {
            const val main = ":client:viewmodel:main"
            const val calculator = ":client:viewmodel:calculator"
            const val currencies = ":client:viewmodel:currencies"
            const val settings = ":client:viewmodel:settings"
            const val selectCurrency = ":client:viewmodel:selectcurrency"
            const val premium = ":client:viewmodel:premium"
            const val watchers = ":client:viewmodel:watchers"
            const val widget = ":client:viewmodel:widget"
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
