package es.ejemplo.android.drawtasks.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.ejemplo.android.drawtasks.R
import es.ejemplo.android.drawtasks.model.Task
import es.ejemplo.android.drawtasks.viewmodel.TaskViewModel

class TasksAdapter(var taskList: MutableList<Task>, val click: (Task, View) -> Unit, val clickLong: (Task, View) -> Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =  inflater.inflate(R.layout.tasks_card, parent, false)
        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if ((holder is TasksViewHolder)) {
            val task = (taskList[position])
            holder.bind(task, click, clickLong)
        }
    }

    override fun getItemCount(): Int = taskList.size

    // Metodo para actualizar la lista según el filtro del Spinner (pending, inProgress, completed)
    fun updateFilteredList(filteredTasks: List<Task>) {
        // Limpiamos la lista de tareas y aplicacmos el filtro para mostrar toda la lista de tareas filtradas dependiendo de que estado queremos
        taskList.clear()
        taskList.addAll(filteredTasks)
        notifyDataSetChanged() // Notificamos que la lista se ha actualizado
    }

    


}