package es.ejemplo.android.drawtasks.model

import java.io.Serializable

data class Task(
    var title: String,
    var description: String,
    var start: String,
    var finish: String,
    var pending: Boolean,
    var inProgress: Boolean,
    var completed: Boolean,
    var ImageResId: Int
): Serializable
