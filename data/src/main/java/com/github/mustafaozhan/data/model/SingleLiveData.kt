/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.model

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveData<T> : LiveData<T>() {
    protected val pending = AtomicBoolean(false)

    @Throws
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
//        todo need to solve
//        if (hasActiveObservers()) {
//            throw SingleLiveDataException()
//        }

        super.observe(owner, Observer {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }
}
