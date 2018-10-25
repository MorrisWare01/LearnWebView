package com.morrisware.android.learnwebview

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by MorrisWare on 2018/8/9.
 * Email: MorrisWare01@gmail.com
 */
fun <T> Lifecycle.registerEvent(eventType: Class<T>, consumer: Consumer<T>) {
    addObserver(object : LifecycleObserver {
        var dispose: Disposable? = null

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate() {
            dispose = RxBus.getInstance().toDefaultFlowable(eventType, consumer)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            dispose?.dispose()
            removeObserver(this)
        }
    })
}
