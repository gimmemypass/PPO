package com.example.second.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.second.Models.TrainingProgram
import com.example.second.R
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener

class EditProgramDialog(
    private var program : TrainingProgram,
    private var listener: OnUpdated
) : DialogFragment()
{

    private lateinit var editView : EditProgramView
   interface OnUpdated{
       fun onUpdated(program: TrainingProgram)
   }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(activity)
        editView = requireActivity().layoutInflater.inflate(R.layout.dialog_program_layout, null) as EditProgramView
        editView.setProgram(program)
        builder.setTitle(getString(R.string.heading_edit_program))
        builder.setView(editView)
        builder.setCancelable(true)
        builder.setNegativeButton(android.R.string.cancel) {
            dialog, which -> dialog.cancel()
        }
        builder.setPositiveButton(android.R.string.ok) {dialog, which ->
            val program = editView.update()
            listener.onUpdated(program)
        }
        return builder.create()
    }

    public fun onColorSelected(color: Int){
        editView.setColor(color)
    }
}