/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File

object ProjectSettings {

    private const val MAYOR_VERSION = 2
    private const val MINOR_VERSION = 11

    // git rev-list --first-parent --count origin/master +1
    private const val VERSION_DIF = 776
    private const val BASE_VERSION_CODE = 937

    const val PROJECT_ID = "com.oztechan.ccc"

    const val ANDROID_APP_ID = "mustafaozhan.github.com.mycurrencies"
    const val HUAWEI_APP_ID = "com.oztechan.ccc.huawei"

    const val COMPILE_SDK_VERSION = 34
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 34

    val JAVA_VERSION = JavaVersion.VERSION_21

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun getVersionCode(project: Project) = try {
        gitCommitCount(project).toInt() + BASE_VERSION_CODE
    } catch (e: Exception) {
        1
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun getVersionName(
        project: Project
    ): String = try {
        if (isMaster(project)) {
            "$MAYOR_VERSION.$MINOR_VERSION.${getVersionCode(project) - VERSION_DIF - BASE_VERSION_CODE}"
        } else {
            "0.0.${getVersionCode(project)}" // testing build
        }.also {
            if (isCI()) project.setIOSVersion(it)
        }
    } catch (e: Exception) {
        "0.0.1"
    }

    private fun gitCommitCount(project: Project): String = project.providers.exec {
        commandLine("git rev-list --first-parent --count HEAD".split(" "))
    }.standardOutput.asText.get().trim()

    private fun isMaster(project: Project): Boolean = project.providers.exec {
        commandLine("git rev-parse --abbrev-ref HEAD".split(" "))
    }.standardOutput.asText.get().trim() == "master"

    private fun isCI() = System.getenv("CI") == "true"

    @Suppress("TooGenericExceptionCaught")
    private fun Project.setIOSVersion(versionName: String) {
        if (System.getProperty("os.name").contains("Mac")) {
            providers.exec {
                workingDir = File("$rootDir/ios")
                commandLine("agvtool new-version -all ${getVersionCode(this@setIOSVersion)}".split(" "))
            }.also {
                // needed for completing the execution
                println("agvtool new-version -all ${it.standardOutput.asText.get()}")
            }
            providers.exec {
                workingDir = File("$rootDir/ios")
                commandLine("agvtool new-marketing-version $versionName".split(" "))
            }.also {
                // needed for completing the execution
                println("agvtool new-marketing-version ${it.standardOutput.asText.get()}")
            }
        } else {
            println("agvtool exist only mac environment")
        }
    }
}
