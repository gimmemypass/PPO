package com.example.second

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.second.Models.Exercise
import com.example.second.Models.ExerciseDetails
import com.example.second.data.RunProgramViewModel
import com.example.second.Models.TrainingProgramDetails
import com.example.second.data.ExerciseViewModel
import com.example.second.presentation.EditExerciseDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ProgramDetailsFragment :
    Fragment(),
    EditExerciseDialog.OnUpdated,
    ExerciseListAdapter.OnItemSelected
{

    private lateinit var mContext: Context
    private lateinit var exerciseViewModel : ExerciseViewModel

    private lateinit var textTitle : TextView
    private lateinit var countExercises : TextView
    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var startButton: FloatingActionButton

    private val runViewModel : RunProgramViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_program_details, container, false)
        textTitle = view.findViewById(R.id.text_title)
        countExercises = view.findViewById(R.id.count_exercises)
        val program = arguments?.getSerializable(PROGMODEL) as TrainingProgramDetails
        textTitle.text = program.trainingProgram.title
        ///////recyclerView
        exerciseRecyclerView = view.findViewById(R.id.exercise_recyclerview)
        val adapter = ExerciseListAdapter(mContext)
        exerciseRecyclerView.adapter = adapter
        exerciseRecyclerView.layoutManager = LinearLayoutManager(context)
        exerciseViewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        exerciseViewModel.searchExercisesByProgramId(program.trainingProgram.programId)
        exerciseViewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            exercises?.let {
                runViewModel.setExercises(it)
                adapter.setExercises(it)
                countExercises.text = it.size.toString()
            }
        })
        //////////////fab
        addButton = view.findViewById(R.id.fab)
        addButton.setOnClickListener{
            val newExercise = Exercise(program.trainingProgram.programId, 0,"", 30000)
            val dialog = EditExerciseDialog(newExercise, this)
            dialog.show(requireActivity().supportFragmentManager, "dialog")
        }
        startButton = view.findViewById(R.id.fab_start)
        startButton.setOnClickListener{
            runViewModel.setUpFragment()
            runViewModel.loadSound(R.raw.notification)
            val runFragment = RunProgramFragment.newInstance()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_layout, runFragment, "runProgramFragment")
                .addToBackStack(null)
                .commit()
        }
        return view
    }

    companion object {
        private const val PROGMODEL = "model"

        @JvmStatic
        fun newInstance(program : TrainingProgramDetails) : ProgramDetailsFragment {
            val args = Bundle().apply {
                putSerializable(PROGMODEL, program)
            }
            val fragment = ProgramDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onUpdated(exercise: Exercise) {
        if(exercise.exerciseId == 0)
            exerciseViewModel.insertExercise(exercise)
        else
            exerciseViewModel.updateExercise(exercise)
    }

    override fun onDeleteItemSelected(exercise: ExerciseDetails) {
        exerciseViewModel.deleteExercise(exercise.exercise)
    }

    override fun onEditItemSelected(exercise: ExerciseDetails) {
        val dialog = EditExerciseDialog(exercise.exercise, this)
        dialog.show(requireActivity().supportFragmentManager, "dialog")
    }
}