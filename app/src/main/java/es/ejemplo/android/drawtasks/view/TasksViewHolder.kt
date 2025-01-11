package es.ejemplo.android.drawtasks.view

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ejemplo.android.drawtasks.R
import es.ejemplo.android.drawtasks.model.Task

class TasksViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title = itemView.findViewById(R.id.taskTitle) as TextView
    val description = itemView.findViewById(R.id.taskDescription) as TextView
    val image = itemView.findViewById(R.id.taskImageView) as ImageView

    fun bind(task: Task, click: (Task, View) -> Unit, clickLong: (Task, View) -> Boolean) {
        title.text = task.title
        description.text = task.description
        image.setImageResource(task.ImageResId)



        itemView.setOnClickListener { click(task, itemView)}
        itemView.setOnLongClickListener { clickLong (task, itemView)}
    }
}