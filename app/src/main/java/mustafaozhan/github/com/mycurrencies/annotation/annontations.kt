package mustafaozhan.github.com.mycurrencies.annotation

import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:43 PM on Arch Linux wit Love <3.
 */
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class PerViewModel