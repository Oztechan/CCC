object SystemEnvironments {
    object Key {
        const val backendUrl = "BASE_URL_BACKEND"
        const val apiUrl = "BASE_URL_API"
    }

    object Value {
        val backendUrl = System.getenv(Key.backendUrl)?.toString() ?: Fake.backendUrl
        val apiUrl = System.getenv(Key.apiUrl)?.toString() ?: Fake.apiUrl
    }

    object Fake {
        const val backendUrl = "http://private.backend.url"
        const val apiUrl = "http://private.api.url"
    }
}
