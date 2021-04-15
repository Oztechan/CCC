import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    with(Plugins) {
        kotlin(multiplatform)
        id(compose)
    }
}

version = ProjectSettings.getVersionName(project)

kotlin {
    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(Modules.client))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "${ProjectSettings.packageName}.desktop.DesktopAppKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
        }
    }
}

// todo remove this when the kotlin and compose version is capable
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xallow-jvm-ir-dependencies",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
}
