package es.ejemplo.android.drawtasks.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

// Creacion de un dialogo a la hora de querer eliminar una tarea de la lista de tareas
class Dialog (var title: String, var content: String, var fragment: Fragment) : DialogFragment() {
    interface OkOrCancel { // Interfaz que las clases hijas (MainScreen) debe implementar los métodos al hacer click en ok o al hacer click en cancelar cuando nos salga el dialogo
        fun onPositiveClick()
        fun onNegativeClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

            builder.setTitle(title).setMessage(content)
                .setPositiveButton(android.R.string.ok) {_, _ ->
                    val listener = fragment as OkOrCancel?
                    listener!!.onPositiveClick()
                }
                .setNegativeButton(android.R.string.cancel) {_, _ ->
                    val listener = fragment as OkOrCancel?
                    listener!!.onNegativeClick()
                }
            return builder.create()
        } ?:throw IllegalStateException("El fragmento no está asociado a ninguna actividad.")
    }
}

