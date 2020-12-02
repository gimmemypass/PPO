package com.example.second.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.second.Models.Exercise
import com.example.second.R
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener

class EditExerciseDialog(
    private var exercise: Exercise,
    private var listener: OnUpdated
) : DialogFragment()
{
    interface OnUpdated{
        fun onUpdated(exercise: Exercise)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(activity)
        val editView = requireActivity().layoutInflater.inflate(R.layout.dialog_exercise_layout, null) as EditExerciseView
        editView.setExercise(exercise)
        builder.setTitle(getString(R.string.heading_edit_exercise))
        builder.setView(editView)
        builder.setCancelable(true)
        builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
            dialog.cancel()
        }
        builder.setPositiveButton(android.R.string.ok){dialog, which ->
            val exercise = editView.update()
            listener.onUpdated(exercise)
        }
        return builder.create()
    }

}