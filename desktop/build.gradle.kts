import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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
