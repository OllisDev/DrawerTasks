package es.ejemplo.android.drawtasks.model

import java.io.Serializable

// Task -> almacenamos los campos que vamos a utilizar a la hora de crear, actualizar o modificar una tarea
data class Task(
    var title: String,
    var description: String,
    var start: String,
    var finish: String,
    var pending: Boolean,
    var inProgress: Boolean,
    var completed: Boolean,
    var ImageResId: Int
): Serializable // Heredamos el data class de Serializable para poder utilizar estos campos de manera sencilla en otras clases
