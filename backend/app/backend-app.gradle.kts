import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    libs.plugins.apply {
        application
        alias(kotlinJvm)
    }
}

tasks.withType<Test> {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("failed")
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
    libs.submob.apply {
        implementation(logmob)
    }

    libs.jvm.apply {
        implementation(koinKtor)
    }

    libs.common.apply {
        implementation(ktorJson)
        implementation(kermit)
    }

    libs.server.apply {
        implementation(ktorCore)
        implementation(ktorNetty)
        implementation(ktorContentNegotiation)
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

    libs.jvm.apply {
        testImplementation(test)
        testImplementation(koinTest)
        // Brings ktor-client-core so the graph test can name HttpClientEngine for verify's extraTypes.
        testImplementation(ktor)
    }
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
