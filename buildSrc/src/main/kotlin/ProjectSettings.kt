/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import org.gradle.api.Project
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object ProjectSettings {

    private const val mayorVersion = 2
    private const val minorVersion = 2

    const val projectId = "mustafaozhan.github.com.mycurrencies"
    const val packageName = "com.github.mustafaozhan.ccc"
    const val compileSdkVersion = 30
    const val minSdkVersion = 21
    const val targetSdkVersion = 30

    fun getVersionCode(project: Project) = gitCommitCount(project).toInt()

    fun getVersionName(project: Project) = "$mayorVersion.$minorVersion.${gitCommitCount(project)}"

    private fun gitCommitCount(project: Project) =
        "git rev-list --first-parent --count origin/master"
            .executeCommand(project.rootDir)?.trim().let {
                if (it.isNullOrEmpty()) 1.toString() else it
            }

    @Suppress("SpreadOperator", "MagicNumber")
    private fun String.executeCommand(workingDir: File): String? = try {
        val parts = this.split("\\s".toRegex())
        ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start().run {
                waitFor(10, TimeUnit.SECONDS)
                inputStream.bufferedReader().readText()
            }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
