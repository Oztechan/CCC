/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object ProjectSettings {

    private const val mayorVersion = 2
    private const val minorVersion = 2

    const val projectId = "mustafaozhan.github.com.mycurrencies"
    const val packageName = "com.github.mustafaozhan.ccc"
    const val compileSdkVersion = 30
    const val minSdkVersion = 21
    const val targetSdkVersion = 30

    fun getVersionCode(project: Project) = gitCommitCount(project).toInt() + 1

    fun getVersionName(project: Project) = "$mayorVersion.$minorVersion.${getVersionCode(project)}"

    private fun gitCommitCount(project: Project): String {
        val os = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-list --first-parent --count HEAD".split(" ")
            standardOutput = os
        }
        return String(os.toByteArray()).trim()
    }
}
