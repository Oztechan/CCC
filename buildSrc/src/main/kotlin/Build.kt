object Build {
    object Type {
        const val DEBUG = "debug"
        const val RELEASE = "release"
    }

    object Flavor {
        const val GOOGLE = "google"
        const val HUAWEI = "huawei"

        fun getFlavorName() = Flavor::class.simpleName.toString()

        @Suppress("unused")
        object Implementation {
            const val googleImplementation = "googleImplementation"
            const val huaweiImplementation = "huaweiImplementation"
            const val googleApi = "googleApi"
            const val huaweiApi = "huaweiApi"
        }
    }
}