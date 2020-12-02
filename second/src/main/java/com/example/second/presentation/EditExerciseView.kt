package com.example.second.presentation

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TableLayout
import com.example.second.Models.Exercise
import com.example.second.R
import com.example.second.data.AppDatabase
import com.example.second.util.InputFilterMinMax
import com.example.second.util.ViewUtils
import java.lang.Exception

class EditExerciseView(context : Context, attrs: AttributeSet) : TableLayout(context, attrs){
    private lateinit var effortSpinner: Spinner
    private lateinit var durationMinutes: EditText
    private lateinit var durationSeconds: EditText
    private lateinit var name: EditText

    private var exercise: Exercise? = null
    private val db = AppDatabase.buildDatabase(context, null)

    override fun onFinishInflate() {
        super.onFinishInflate()
        effortSpinner = findViewById(R.id.exercise_edit_effort_level)
        val effortAdapter : SpinnerAdapter = EffortTypeAdapter(context, db.exerciseDao())
        effortSpinner.adapter = effortAdapter
        durationMinutes = findViewById(R.id.exercise_edit_duration_minutes)
        durationSeconds = findViewById(R.id.exercise_edit_duration_seconds)
        durationMinutes.filters = arrayOf<InputFilter>(InputFilterMinMax("0", "99"))
        durationSeconds.filters = arrayOf<InputFilter>(InputFilterMinMax("0", "59"))
        name = findViewById(R.id.exercise_edit_name)

        if(exercise != null)
            render()
    }

    public fun update() : Exercise {
        exercise!!.duration = getDuration()
        exercise!!.effortTypeFk = effortSpinner.selectedItemPosition
        exercise!!.name = name.text.toString()

        return exercise!!
    }

    private fun getDuration() : Int{
        var totalDuration : Int = 0;
        totalDuration += ViewUtils.getIntFromTextViewSafe(durationMinutes)*60
        totalDuration += ViewUtils.getIntFromTextViewSafe(durationSeconds)
        totalDuration *= 1000 //to miliseconds
        return totalDuration
    }

    public fun setExercise(exercise: Exercise){
        this.exercise = exercise
        render()
    }

    private fun render(){
        effortSpinner.setSelection(exercise!!.effortTypeFk)
        durationMinutes.setText(String.format("%02d", ViewUtils.getMinutesFromMs(exercise!!.duration)))
        durationSeconds.setText(String.format("%02d", ViewUtils.getSecondsFromMs(exercise!!.duration)))
        name.setText(exercise!!.name)
    }

}