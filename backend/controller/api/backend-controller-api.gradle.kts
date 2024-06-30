import dev.mokkery.gradle.ApplicationRule

plugins {
    libs.plugins.apply {
        alias(kotlinJvm)
        alias(mokkery)
    }
}

mokkery.rule.set(ApplicationRule.Listed("test"))

dependencies {
    libs.common.apply {
        implementation(kermit)
        implementation(koinCore)

        testImplementation(coroutinesTest)
        testImplementation(test)
    }

    Modules.Common.Core.apply {
        implementation(project(network))
        implementation(project(model))
    }

    Modules.Common.DataSource.apply {
        implementation(project(conversion))
    }
}
