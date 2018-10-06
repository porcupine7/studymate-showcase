package at.fhooe.studymate.entities

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import at.fhooe.studymate.fragments.TaskFragment

class TaskModel : ViewModel() {
    lateinit var callback: TaskFragment.Callback

    var title = "A Nice Title"

    var description = "Desc"

    var instruction = "Do this"

    var running: ObservableField<Boolean> = ObservableField()

    fun onStart() {
        Log.d("uptown", "Start Clicked")
        callback.startTask()
        running.set(true)
    }

    fun onConcede() {
        Log.d("uptown", "Concede Clicked")
        callback.concedeTask()
        running.set(false)
    }

}