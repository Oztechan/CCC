/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.io.File

object ProjectSettings {

    private const val MAYOR_VERSION = 2
    private const val MINOR_VERSION = 5

    // git rev-list --first-parent --count master +1
    private const val VERSION_DIF = 713

    const val PROJECT_ID = "mustafaozhan.github.com.mycurrencies"
    const val HUAWEI_APP_ID = "com.oztechan.ccc.huawei"
    const val PACKAGE_NAME = "com.oztechan.ccc"
    const val COMPILE_SDK_VERSION = 31
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 31

    fun getVersionCode(project: Project) = gitCommitCount(project).toInt()

    fun getVersionName(
        project: Project
    ): String = if (isMaster(project)) {
        "$MAYOR_VERSION.$MINOR_VERSION.${getVersionCode(project) - VERSION_DIF}"
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

    private fun isCI() = System.getProperties()["idea.platform.prefix"] == null

    private fun Project.setIOSVersion(versionName: String) {
        exec {
            workingDir = File("${project.rootDir}/ios")
            commandLine = "agvtool new-version -all ${getVersionCode(project)}".split(" ")
        }
        exec {
            workingDir = File("${project.rootDir}/ios")
            commandLine = "agvtool new-marketing-version $versionName".split(" ")
        }
    }
}
