import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    with(Plugins) {
        kotlin(MULTIPLATFORM)
        id(COMPOSE)
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
                implementation(project(Modules.CLIENT))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "${ProjectSettings.PACKAGE_NAME}.desktop.DesktopAppKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
        }
    }
}
