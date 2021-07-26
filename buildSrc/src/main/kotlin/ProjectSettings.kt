/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object ProjectSettings {

    private const val MAYOR_VERSION = 2
    private const val MINOR_VERSION = 2

    const val PROJECT_ID = "mustafaozhan.github.com.mycurrencies"
    const val PACKAGE_NAME = "com.github.mustafaozhan.ccc"
    const val COMPILE_SDK_VERSION = 30
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 30

    fun getVersionCode(project: Project) = gitCommitCount(project).toInt()

    fun getVersionName(project: Project) =
        "$MAYOR_VERSION.$MINOR_VERSION.${gitCommitCount(project)}"

    private fun gitCommitCount(project: Project): String {
        val os = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-list --first-parent --count HEAD".split(" ")
            standardOutput = os
        }
        return String(os.toByteArray()).trim()
    }
}
