plugins {
    libs.plugins.apply {
        application
        alias(kotlinJvm)
    }
}

ProjectSettings.apply {
    group = PROJECT_ID
    version = getVersionName(project)
}

application {
    mainClass.set("${Modules.Backend.app.packageName}.ApplicationKt")
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
    }

    Modules.Common.Core.apply {
        implementation(project(database))
        implementation(project(network))
        implementation(project(infrastructure))
    }

    Modules.Backend.Service.apply {
        implementation(project(premium))
    }

    Modules.Backend.Controller.apply {
        implementation(project(sync))
        implementation(project(api))
    }

    Modules.Common.DataSource.apply {
        implementation(project(conversion))
    }

    implementation(Submodules.logmob)
}

tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Version"] = ProjectSettings.getVersionName(project)
        attributes["Main-Class"] = "${Modules.Backend.app.packageName}.ApplicationKt"
    }
    from(
        configurations.runtimeClasspath.get().map {
            it.takeIf { it.isDirectory } ?: zipTree(it)
        }
    )
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
