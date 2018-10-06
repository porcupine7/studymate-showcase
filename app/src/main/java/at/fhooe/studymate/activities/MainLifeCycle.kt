package at.fhooe.studymate.activities

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log

class MainLifeCycle : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun started() {
        Log.d("MainLifeCycle", "Lifecycle: onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopped() {
        Log.d("MainLifeCycle", "Lifecycle: onStop")
    }
}