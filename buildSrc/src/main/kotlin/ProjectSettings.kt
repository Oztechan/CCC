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
        "git rev-list --first-parent --count HEAD"
            .runCommand(project.rootDir).trim().let {
                if (it.isEmpty()) 1.toString() else it
            }

    private fun String.runCommand(
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
    ): String = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
        .apply { waitFor(timeoutAmount, timeoutUnit) }
        .run {
            val error = errorStream.bufferedReader().readText().trim()
            if (error.isNotEmpty()) {
                throw IOException(error)
            }
            inputStream.bufferedReader().readText().trim()
        }
}
