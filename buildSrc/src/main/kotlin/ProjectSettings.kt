/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import org.gradle.api.Project

object ProjectSettings {

    private const val mayorVersion = 2
    private const val minorVersion = 1

    const val projectId = "mustafaozhan.github.com.mycurrencies"
    const val projectCompileSdkVersion = 29
    const val projectMinSdkVersion = 21
    const val projectTargetSdkVersion = 29

    fun getVersionCode(project: Project) = gitCommitCount(project).let {
        if (it.isEmpty()) 1 else it.toInt()
    }

    fun getVersionName(project: Project) = "$mayorVersion.$minorVersion.${gitCommitCount(project)}"

    private fun gitCommitCount(project: Project) =
        "git rev-list --first-parent --count origin/master"
            .executeCommand(project.rootDir)?.trim()
            ?: ""

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
