package es.ejemplo.android.drawtasks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ejemplo.android.drawtasks.model.Task

class TaskViewModel : ViewModel() {
    private val _task = MutableLiveData<MutableList<Task>>(mutableListOf())
    val task: LiveData<MutableList<Task>> get() =_task

    // Método para agregar una tarea
    fun addTask(task: Task) {
        val taskList = _task.value?.toMutableList() ?: mutableListOf()
        if (!taskList.contains(task)) { // Verificar si la tarea ya existe
            taskList.add(task)
            _task.value = taskList
        }
    }

    // Obtener la lista de las tareas
    fun getTask(): List<Task> {
        return _task.value?: listOf()
    }

    // Método para eliminar una tarea
    fun removeTask(task: Task) {
        val updatedList = _task.value ?: mutableListOf()
        updatedList.remove(task)
        _task.value = updatedList
    }

    fun updateTask(oldTask: Task, newTask: Task) {
        val taskList = _task.value?.toMutableList() ?: mutableListOf()
        val index = taskList.indexOfFirst { it == oldTask }
        if (index != -1) {
            taskList[index] = newTask // Actualizamos la tarea
            _task.value = taskList // Notificamos el cambio a los observadores
        }
    }

}