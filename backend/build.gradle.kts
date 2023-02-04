plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        application
        id(jvm.get().pluginId)
        alias(ksp)
    }
}

ProjectSettings.apply {
    group = PROJECT_ID
    version = getVersionName(project)
}

application {
    mainClass.set("${Modules.Backend.self.packageName}.ApplicationKt")
}

dependencies {
    libs.jvm.apply {
        implementation(ktorCore)
        implementation(ktorNetty)
        implementation(koinKtor)
    }

    libs.common.apply {
        implementation(ktorServerContentNegotiation)
        implementation(ktorJson)
        implementation(kermit)

        testImplementation(mockative)
        testImplementation(coroutinesTest)
        testImplementation(test)

        ksp(libs.processors.mockative)
    }

    Modules.Common.Core.apply {
        implementation(project(database))
        implementation(project(network))
        implementation(project(infrastructure))
        implementation(project(model))
    }

    Modules.Backend.Service.apply {
        implementation(project(free))
        implementation(project(premium))
    }

    Modules.Common.DataSource.apply {
        implementation(project(conversion))
    }

    implementation(project(Modules.Submodules.logmob))
}

ksp {
    arg("mockative.stubsUnitByDefault", "true")
}

tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Version"] = ProjectSettings.getVersionName(project)
        attributes["Main-Class"] = "${Modules.Backend.self.packageName}.ApplicationKt"
    }
    from(
        configurations.runtimeClasspath.get().map {
            it.takeIf { it.isDirectory } ?: zipTree(it)
        }
    )
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
