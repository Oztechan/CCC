package mustafaozhan.github.com.mycurrencies.base.api

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.Scheduler
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:46 PM on Arch Linux wit Love <3.
 */
class RxErrorHandlingCallAdapterFactory : CallAdapter.Factory {
    companion object {
        fun create(): CallAdapter.Factory = RxErrorHandlingCallAdapterFactory()
        fun create(io: Scheduler): CallAdapter.Factory = RxErrorHandlingCallAdapterFactory(io)
    }

    private val original: RxJava2CallAdapterFactory

    private constructor() {
        original = RxJava2CallAdapterFactory.create()
    }

    private constructor(scheduler: Scheduler) {
        original = RxJava2CallAdapterFactory.createWithScheduler(scheduler)
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        val wrapped = original.get(returnType, annotations, retrofit) as? CallAdapter<out Any, *>
        return RxCallAdapterWrapper(retrofit, wrapped)
    }

    private class RxCallAdapterWrapper<R>(
        private val retrofit: Retrofit,
        private val wrapped: CallAdapter<R, *>?
    ) : CallAdapter<R, Observable<R>> {

        override fun responseType(): Type? {
            return wrapped?.responseType()
        }

        @SuppressLint("CheckResult")
        @Suppress("UNCHECKED_CAST")
        override fun adapt(call: Call<R>): Observable<R>? {
            val adapted = (wrapped?.adapt(call) as? Observable<R>)
            adapted?.onErrorResumeNext { throwable: Throwable ->
                Observable.error(asRetrofitException(throwable))
            }

            return adapted
        }

        private fun asRetrofitException(throwable: Throwable) = when (throwable) {
            is HttpException -> RetrofitException.httpError(
                throwable.response().raw().request().url().toString(),
                throwable.response(),
                retrofit
            )
            is IOException -> RetrofitException.networkError(throwable)
            else -> RetrofitException.unexpectedError(throwable)
        }
    }
}
