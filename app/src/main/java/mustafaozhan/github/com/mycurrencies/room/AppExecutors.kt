package mustafaozhan.github.com.mycurrencies.room

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Singleton
class AppExecutors(val diskIO: Executor, val networkIO: Executor, val mainThread: Executor) {

    companion object {
        const val THREAD_COUNT = 3
    }

//    val diskIO: Executor = DiskIOThreadExecutor()
//    val networkIO: Executor = Executors.newFixedThreadPool(THREAD_COUNT)
//    val mainThread: Executor = MainThreadExecutor()


}

class MainThreadExecutor : Executor {
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        mainThreadHandler.post(command)
    }
}

class DiskIOThreadExecutor : Executor {

    private val mDiskIO: Executor

    init {
        mDiskIO = Executors.newSingleThreadExecutor()
    }

    override fun execute(command: Runnable) {
        mDiskIO.execute(command)
    }
}