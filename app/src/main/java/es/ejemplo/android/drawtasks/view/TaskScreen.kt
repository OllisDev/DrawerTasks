package es.ejemplo.android.drawtasks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import es.ejemplo.android.drawtasks.R
import es.ejemplo.android.drawtasks.databinding.TaskScreenBinding
import es.ejemplo.android.drawtasks.model.Task
import es.ejemplo.android.drawtasks.viewmodel.TaskViewModel

class TaskScreen : Fragment(R.layout.task_screen), Dialog.OkOrCancel {
    private var tasksAdapter: TasksAdapter? = null
    private var taskViewModel: TaskViewModel? = null
    var _binding:TaskScreenBinding? = null

    private val binding get() = _binding!!

    private var ActualTask: Task?

    init {
        ActualTask = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = TaskScreenBinding.inflate(inflater, container, false)

        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        populateSpinner()


        val task: Task? = arguments?.getSerializable("task") as Task?

        binding.Image.setImageResource(R.drawable.task)

        binding.cbPending.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbInProgress.isChecked = false
                binding.cbCompleted.isChecked = false
            }
        }

        binding.cbInProgress.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbPending.isChecked = false
                binding.cbCompleted.isChecked = false
            }
        }

        binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbPending.isChecked = false
                binding.cbInProgress.isChecked = false
            }
        }

        task?.let {
            ActualTask = it
            binding.Title.setText(it.title)
            binding.Description.setText(it.description)

            for (i in 0 .. binding.SpinnerStart.adapter.count)
                if (binding.SpinnerStart.adapter.getItem(i).equals(it.start)) {
                    binding.SpinnerStart.setSelection(i)
                    break
                }
            for (i in 0 .. binding.SpinnerFinish.adapter.count)
                if (binding.SpinnerFinish.adapter.getItem(i).equals(it.finish)) {
                    binding.SpinnerFinish.setSelection(i)
                    break
                }

            if (it.pending ?: false) binding.cbPending.isChecked = true
            if (it.inProgress ?: false) binding.cbInProgress.isChecked = true
            if (it.completed ?: false) binding.cbCompleted.isChecked = true
        }

        binding.btSave.setOnClickListener {
            val dialog = Dialog(resources.getString(R.string.SaveData), resources.getString(R.string.AskSaveData), this)
            dialog.show(requireActivity().supportFragmentManager, "Dialog")
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun populateSpinner() {
        ArrayAdapter.createFromResource(requireActivity(), R.array.horas, android.R.layout.simple_spinner_dropdown_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            binding.SpinnerFinish.adapter = adapter
        }
    }


    override fun onPositiveClick() {
        val updatedTask = Task (
            binding.Title.text.toString(),
            binding.Description.text.toString(),
            binding.SpinnerStart.selectedItem.toString(),
            binding.SpinnerFinish.selectedItem.toString(),
            binding.cbPending.isChecked,
            binding.cbInProgress.isChecked,
            binding.cbCompleted.isChecked,
            R.drawable.task
        )

        if (ActualTask != null) {
            taskViewModel?.updateTask(ActualTask!!, updatedTask)
        } else {
            taskViewModel?.addTask(updatedTask)
        }

        taskViewModel?.task?.observe(viewLifecycleOwner) { taskList ->
            tasksAdapter?.taskList = taskList
            tasksAdapter?.notifyDataSetChanged()
        }

        ActualTask = updatedTask.copy()

        Snackbar.make(binding.root, resources.getString(R.string.dataSaved), Snackbar.LENGTH_LONG)
    }

    override fun onNegativeClick() {
        Toast.makeText(requireActivity().applicationContext, resources.getString(R.string.actionCancelled), Toast.LENGTH_SHORT)
    }
}