package es.ejemplo.android.drawtasks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import es.ejemplo.android.drawtasks.R
import es.ejemplo.android.drawtasks.databinding.MainScreenBinding
import es.ejemplo.android.drawtasks.model.Task
import es.ejemplo.android.drawtasks.viewmodel.TaskViewModel

class MainScreen : Fragment(R.layout.main_screen), Dialog.OkOrCancel {
    private var removeTask: Task? // Almacenamiento de la tarea para poder eliminarlo cuando lo seleccionamos

    private var tasksAdapter: TasksAdapter? = null
    lateinit var taskViewModel: TaskViewModel



    private var _binding: MainScreenBinding? = null // Almacenamiento mediante variable del los IDs de los componentes del layout de MainScreen (Binding)

    private val binding get() = _binding!! // IMPORTANTE: añadir !! para poder modificar el binding durante el ciclo de vida de la aplicacion almacenado en una variable
    init {
        removeTask = null // Inicialización de la variable para eliminar la tarea
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java) // Inicializacion del ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Sobrecargar la vista del fragmento del MainScreen mediante Binding
        _binding = MainScreenBinding.inflate(inflater, container, false)
        // Configuración del RecyclerView y mostrar lista de tareas
        binding.TasksRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.TasksRecyclerView.setHasFixedSize(true)
        val taskList = mutableListOf<Task>()
        tasksAdapter = TasksAdapter(
            taskList,
            { task: Task, v: View -> clicked(task, v) },
            { task: Task, v: View -> clickedLong(task, v) }
        )
        binding.TasksRecyclerView.adapter = tasksAdapter

        taskViewModel.task.observe(viewLifecycleOwner) { tasklist ->
            tasksAdapter?.taskList = tasklist
            tasksAdapter?.notifyDataSetChanged()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clicked(task: Task, v: View) {
        val bundle = bundleOf("task" to task)
        v.findNavController().navigate(R.id.action_nav_tasks_to_nav_createTasks, bundle)
    }


    private fun clickedLong(task: Task, v: View): Boolean {
        removeTask = task

        val dialog = Dialog(resources.getString(R.string.deletePark), resources.getString(R.string.askDeletePark) + task.title + "?", this)
        dialog.show(requireActivity().supportFragmentManager, "Dialog")
        return true
    }


    override fun onPositiveClick() {
        removeTask?.let { task ->
            taskViewModel.removeTask(task)
        }
    }

    override fun onNegativeClick() {
        Toast.makeText(requireActivity().applicationContext, resources.getString(R.string.actionCancelled), Toast.LENGTH_SHORT).show()
    }



}