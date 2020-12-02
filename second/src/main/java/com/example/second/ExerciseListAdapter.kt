package com.example.second

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.second.Models.ExerciseDetails
import com.example.second.Models.TrainingProgram
import com.example.second.Models.TrainingProgramDetails
import com.example.second.util.ViewUtils

class ExerciseListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder>(){

    interface OnItemSelected{
        fun onDeleteItemSelected(exercise: ExerciseDetails)
        fun onEditItemSelected(exercise: ExerciseDetails)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var exercises = emptyList<ExerciseDetails>() // Cached copy of words

    private lateinit var listener : OnItemSelected
    private val mContext: Context = context
    init {
        val fragment = (context as AppCompatActivity).supportFragmentManager.findFragmentByTag("detailsFragment")
        if(fragment is OnItemSelected)
            listener = fragment
    }
    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseItemTitle: TextView = itemView.findViewById(R.id.textTitle)
        val exerciseItemDurationMinutes: TextView = itemView.findViewById(R.id.textDurationMinutes)
        val exerciseItemDurationSeconds: TextView = itemView.findViewById(R.id.textDurationSeconds)
        val exerciseItemEF: TextView = itemView.findViewById(R.id.textEffortType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListAdapter.ExerciseViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_exercise, parent, false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseListAdapter.ExerciseViewHolder, position: Int) {
        val current = exercises[position]
        holder.exerciseItemTitle.text = current.exercise.name
        holder.exerciseItemDurationMinutes.text = ViewUtils.getMinutesFromMs(current.exercise.duration).toString()
        holder.exerciseItemDurationSeconds.text = ViewUtils.getSecondsFromMs(current.exercise.duration).toString()
        holder.exerciseItemEF.text = current.effortType.type
        holder.exerciseItemEF.setBackgroundColor(Color.parseColor(current.effortType.color))
        holder.itemView.setOnLongClickListener{
            val actions = arrayOf<String>("Delete", "Edit")
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Select action")
            builder.setItems(actions) { _, which ->
                when(which){
                    0 -> {
                        listener.onDeleteItemSelected(current)
                    }
                    1 -> {
                        listener.onEditItemSelected(current)
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
            true
        }
    }

    internal fun setExercises(exercises: List<ExerciseDetails>) {
        this.exercises = exercises
        notifyDataSetChanged()
    }

    override fun getItemCount() = exercises.size
}
