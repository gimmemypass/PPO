package com.example.second

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.second.Models.TrainingProgram
import com.example.second.Models.TrainingProgramDetails

class ProgramListAdapter internal constructor(
 context: Context
) : RecyclerView.Adapter<ProgramListAdapter.ProgramViewHolder>(){

    interface OnItemSelected{
        fun onItemSelected(program: TrainingProgramDetails)
        fun onDeleteItemSelected(program: TrainingProgramDetails)
        fun onEditItemSelected(program: TrainingProgramDetails)
    }
 private val inflater: LayoutInflater = LayoutInflater.from(context)
 private var programs = emptyList<TrainingProgramDetails>() // Cached copy of words
 private lateinit var listener : OnItemSelected
 private val mContext: Context = context
 init {
     val fragment = (context as AppCompatActivity).supportFragmentManager.findFragmentByTag("programListFragment")
     if(fragment is OnItemSelected)
      listener = fragment
 }
 inner class ProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val programTitleView: TextView = itemView.findViewById(R.id.recycler_text_view)
  val programColorView: View = itemView.findViewById(R.id.recycler_color_view)
 }

 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramListAdapter.ProgramViewHolder {
  val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
  return ProgramViewHolder(itemView)
 }

 override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
  val current = programs[position]
  holder.programTitleView.text = current.trainingProgram.title
  holder.programColorView.setBackgroundColor(Color.parseColor(current.trainingProgram.color))
  holder.itemView.setOnClickListener{
   listener.onItemSelected(current)
  }
  holder.itemView.setOnLongClickListener{
      val actions = arrayOf<String>("Select", "Delete", "Edit")
      val builder = AlertDialog.Builder(mContext)
      builder.setTitle("Select action")
      builder.setItems(actions) { _, which ->
          when(which){
              0 ->{
                  listener.onItemSelected(current)
              }
              1 -> {
                  listener.onDeleteItemSelected(current)
              }
              2 -> {
                  listener.onEditItemSelected(current)
              }
          }
      }
      val dialog = builder.create()
      dialog.show()
      true
  }
 }

 internal fun setPrograms(programs: List<TrainingProgramDetails>) {
  this.programs = programs
  notifyDataSetChanged()
 }

 override fun getItemCount() = programs.size
}