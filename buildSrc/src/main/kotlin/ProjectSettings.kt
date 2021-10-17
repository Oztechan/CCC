/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import java.io.ByteArrayOutputStream
import org.gradle.api.Project

object ProjectSettings {

    private const val MAYOR_VERSION = 2
    private const val MINOR_VERSION = 3

    // git rev-list --first-parent --count master +1
    private const val VERSION_DIF = 691

    const val PROJECT_ID = "mustafaozhan.github.com.mycurrencies"
    const val PACKAGE_NAME = "com.github.mustafaozhan.ccc"
    const val COMPILE_SDK_VERSION = 31
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 31

    fun getVersionCode(project: Project) = gitCommitCount(project).toInt()

    fun getVersionName(
        project: Project
    ) = if (isMaster(project)) {
        "$MAYOR_VERSION.$MINOR_VERSION.${gitCommitCount(project).toInt() - VERSION_DIF}"
    } else {
        gitCommitCount(project) // testing build
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
        return String(os.toByteArray()) == "master"
    }
}
