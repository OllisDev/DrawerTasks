package es.ejemplo.android.drawtasks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
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

// MainScreen -> fragmento que almacenaremos todos los CardViews de las tareas creadas del TaskScreen

class MainScreen : Fragment(R.layout.main_screen), Dialog.OkOrCancel {
    private var removeTask: Task? // Almacenamiento de la tarea para poder eliminarlo cuando lo seleccionamos
    private var tasksAdapter: TasksAdapter? = null // Almacenar e inicializar instancia de la clase TaskAdapter
    lateinit var taskViewModel: TaskViewModel // Almacenar e inicializar instancia del ViewModel



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
        binding.TasksRecyclerView.setHasFixedSize(true) // Ajustamos el tamaño del RecyclerView del layout
        val taskList = mutableListOf<Task>()
        tasksAdapter = TasksAdapter(
            taskList,
            { task: Task, v: View -> clicked(task, v) },
            { task: Task, v: View -> clickedLong(task, v) }
        )
        binding.TasksRecyclerView.adapter = tasksAdapter


        // Almacenamos en una variable el Spinner de filtrar según el estado de la tarea mediante el almacenamiento de un string array en la carpeta de String
        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_options,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.FilterSpinner.adapter = spinnerAdapter

        binding.FilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilter(position) // Aplicar el filtro según la opción seleccionada
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        // Actualizamos con observe la lista filtrada de tareas segun el estado de la tarea
        taskViewModel.task.observe(viewLifecycleOwner) { tasklist ->
            tasksAdapter?.updateFilteredList(tasklist)

        }

        return binding.root
    }

    // Metodo para aplicar los filtros mediante el Spinner según el estado de la tarea (pending, inProgess, completed)
    private fun applyFilter(position: Int) {
        val filteredTask = when (position) {
            0 -> taskViewModel.task.value
            1 -> taskViewModel.task.value?.filter { it.pending } // Filtrar pendientes
            2 -> taskViewModel.task.value?.filter { it.inProgress } // Filtrar en progreso
            3 -> taskViewModel.task.value?.filter { it.completed } // Filtrar completadas
            else -> taskViewModel.task.value // Default: Mostrar todas
        }

        filteredTask?.let{
            tasksAdapter?.updateFilteredList(it) // Llamamos al metodo updateFilteredList de la clase TasksAdapter para poder recorrer la lista y aplicar la actualizacion de la lista de tareas
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Método que al hacer un click, abre el TaskScreen para poder modificar la tarea creada
    private fun clicked(task: Task, v: View) {
        val bundle = bundleOf("task" to  task) // bundleOf -> permite generar la tarea para pasar del MainScreen al TaskScreen mediante clave-valor
        v.findNavController().navigate(R.id.action_nav_tasks_to_nav_createTasks, bundle) // La vista se encargará de pasar del MainScreen al TaskScreen mediante el almacenamiento de la funcionalidad proporcionada del mobile_navigation.xnl
    }

    // Método que al mantener el click, abre un dialog que nos dirá si queremos eliminar o no la tarea seleccionada
    private fun clickedLong(task: Task, v: View): Boolean {
        removeTask = task // Guardamos en la variable removeTask la tarea que hemos seleccionado

        val dialog = Dialog(resources.getString(R.string.deletePark), resources.getString(R.string.askDeletePark) + " " + task.title + "?", this) // Llamamos a la clase Dialog para aplicar la frase de eliminar el parque a la hora de que nos abra el dialogo y concatenamos el titulo de la tarea seleccionada
        dialog.show(requireActivity().supportFragmentManager, "Dialog") // Mostramos el dialogo
        return true
    }

    // Metodo de la interfaz guardada en la clase del dialogo para que implemente que cuando pulse Aceptar elimine la tarea seleccionada
    override fun onPositiveClick() {
        // Elimina la tarea a través de llamar el método removeTask de la clase TaskViewModel
        removeTask?.let { task ->
            taskViewModel.removeTask(task)
        }
    }

    // Método de la interfaz guardada en la clase del dialogo para que implemente que cuando pulse Cancelar salga un mensaje emergente (Toast) de la accion ha sido cancelada y no elimine la tarea
    override fun onNegativeClick() {
        Toast.makeText(requireActivity().applicationContext, resources.getString(R.string.actionCancelled), Toast.LENGTH_SHORT).show()
    }



}