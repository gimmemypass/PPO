package com.example.second.presentation

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.second.Models.TrainingProgram
import com.example.second.R
import com.example.second.data.AppDatabase
import com.example.second.util.ViewUtils
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jaredrummler.android.colorpicker.ColorShape

class EditProgramView(context: Context, attrs: AttributeSet) :
    TableLayout(context, attrs)
{
    private lateinit var titleView: TextView
    private lateinit var colorView: View

    private var program: TrainingProgram? = null
    private val db = AppDatabase.buildDatabase(context, null)

    override fun onFinishInflate() {
        super.onFinishInflate()
        titleView = findViewById(R.id.program_edit_title)
        colorView = findViewById(R.id.program_edit_color)
        colorView.setOnClickListener{
            createColorPickerDialog(0)
        }
        if(program != null)
            render()
    }

    public fun update() : TrainingProgram{
        program!!.title = titleView.text.toString()
        program!!.color = getColor()
        return program!!
    }

    private fun getColor() : String{
        val color = (colorView.background as? ColorDrawable)?.color
        return ViewUtils.stringColorFromInt(color!!)
    }

    public fun setProgram(program: TrainingProgram){
        this.program = program
        render()
    }

    private fun render(){
        titleView.text = program!!.title
        colorView.setBackgroundColor(Color.parseColor(program!!.color))
    }

    private fun createColorPickerDialog(id : Int){
       ColorPickerDialog.newBuilder()
           .setColor(Color.RED)
           .setDialogType(ColorPickerDialog.TYPE_PRESETS)
           .setAllowCustom(true)
           .setAllowPresets(true)
           .setColorShape(ColorShape.CIRCLE)
           .setDialogId(id)
           .show(context as? FragmentActivity)
    }

    public fun setColor(color: Int){
        colorView.setBackgroundColor(color)
    }
}