/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.io.File

object ProjectSettings {

    private const val MAYOR_VERSION = 2
    private const val MINOR_VERSION = 7

    // git rev-list --first-parent --count master +1
    private const val VERSION_DIF = 728
    private const val BASE_VERSION_CODE = 316

    const val PROJECT_ID = "com.oztechan.ccc"

    const val ANDROID_APP_ID = "mustafaozhan.github.com.mycurrencies"
    const val HUAWEI_APP_ID = "com.oztechan.ccc.huawei"

    const val COMPILE_SDK_VERSION = 31
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 31

    fun getVersionCode(project: Project) = gitCommitCount(project).toInt() + BASE_VERSION_CODE

    fun getVersionName(
        project: Project
    ): String = if (isMaster(project)) {
        "$MAYOR_VERSION.$MINOR_VERSION.${getVersionCode(project) - VERSION_DIF - BASE_VERSION_CODE}"
    } else {
        "0.0.${getVersionCode(project)}" // testing build
    }.also {
        if (isCI()) project.setIOSVersion(it)
    }

    private fun gitCommitCount(project: Project): String {
        val os = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-list --first-parent --count HEAD".split(" ")
            standardOutput = os
        }
        return String(os.toByteArray()).trim()
    }

    private fun isMaster(project: Project): Boolean {
        val os = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-parse --abbrev-ref HEAD".split(" ")
            standardOutput = os
        }
        return String(os.toByteArray()).trim() == "master"
    }

    private fun isCI() = System.getenv("CI") == "true"

    @Suppress("TooGenericExceptionCaught")
    private fun Project.setIOSVersion(versionName: String) = try {
        exec {
            workingDir = File("${project.rootDir}/ios")
            commandLine = "agvtool new-version -all ${getVersionCode(project)}".split(" ")
        }
        exec {
            workingDir = File("${project.rootDir}/ios")
            commandLine = "agvtool new-marketing-version $versionName".split(" ")
        }
    } catch (e: Exception) {
        println("agvtool exist only mac environment")
        println(e.localizedMessage)
    }
}
