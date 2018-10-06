package at.fhooe.studymate.entities

import android.arch.lifecycle.ViewModel
import android.util.Log
import at.fhooe.studymate.fragments.TaskFragment

class TaskModel : ViewModel() {
    lateinit var callback: TaskFragment.Callback

    var title = "A Nice Title"

    var description = "Desc"

    var instruction = "Do this"

    var running = false

    fun onStart() {
        Log.d("uptown", "Start Clicked")
        if (!running) {
            callback.startTask()
            running = true
        }
    }

    fun onConcede() {
        Log.d("uptown", "Concede Clicked")
        if (running) {
            callback.concedeTask()
            running = false
        }
    }

}