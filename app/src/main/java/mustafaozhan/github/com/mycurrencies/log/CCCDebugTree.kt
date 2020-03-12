package mustafaozhan.github.com.mycurrencies.log

import android.content.Context
import android.util.Log
import mustafaozhan.github.com.mycurrencies.extension.toFormattedString
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.Date

@Suppress("LogNotTimber", "TooGenericExceptionCaught")
class CCCDebugTree(context: Context) : Timber.DebugTree() {

    companion object {
        private const val MAX_FILE_SIZE = (10 * 1024 * 1024).toLong() // 10 MB
        private const val TAG = "CCCDebugTree"
    }

    private val currentFilePath: String
    private val lastFilePath: String

    init {
        val directoryPath = context.cacheDir.toString() + "/logs/"
        lastFilePath = directoryPath + context.packageName + "_last_log.txt"
        currentFilePath = directoryPath + context.packageName + "_current_log.txt"

        val directory = File(directoryPath)
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e(TAG, "Failed to create LOG file directory")
        }

        Timber.plant(this)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        var realMessage = message
        if (message.isNotEmpty()) {
            realMessage = "@" + Thread.currentThread().name + ": " + message
            appendLine(priorityToLevel(priority), tag, message, t)
        }

        super.log(priority, tag, realMessage, t)
    }

    private fun appendLine(level: String, tag: String?, line: String, throwable: Throwable?) {
        rolloverIfNeeded()

        var outputStream: PrintWriter? = null
        try {
            outputStream = PrintWriter(FileOutputStream(currentFilePath, true))
            outputStream.printf("%s %s/%s: %s\n", Date().toFormattedString(), level, tag, line)
            if (throwable != null) {
                outputStream.printf("Caused by: %s\n", Log.getStackTraceString(throwable))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write to LOG file", e)
        } finally {
            outputStream?.close()
        }
    }

    private fun rolloverIfNeeded() {
        val lastFile = File(lastFilePath)
        val currentFile = File(currentFilePath)

        if (!currentFile.exists()) {
            return
        }

        if (currentFile.length() <= MAX_FILE_SIZE) {
            return
        }

        try {
            copyFile(currentFile, lastFile, true)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to copy log file", e)
        }
    }

    @Suppress("SameParameterValue")
    @Throws(Exception::class)
    private fun copyFile(src: File, dst: File, deleteSource: Boolean) {
        val inStream = FileInputStream(src)
        val outStream = FileOutputStream(dst)
        val inChannel = inStream.channel
        val outChannel = outStream.channel
        inChannel.transferTo(0, inChannel.size(), outChannel)
        inStream.close()
        outStream.close()

        Log.d(TAG, "Data copied from $src to $dst")

        if (deleteSource) {
            if (src.delete()) {
                Log.d(TAG, "Source file deleted")
            } else {
                Log.e(TAG, "Failed to delete source file!")
            }
        }
    }

    private fun priorityToLevel(priority: Int) = when (priority) {
        Log.VERBOSE -> "V"
        Log.DEBUG -> "D"
        Log.INFO -> "I"
        Log.WARN -> "W"
        Log.ERROR -> "E"
        Log.ASSERT -> "A"
        else -> "?"
    }
}
