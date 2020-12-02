package com.example.second

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.second.Models.TrainingProgram
import com.example.second.Models.TrainingProgramDetails
import com.example.second.data.ProgramViewModel
import com.example.second.presentation.EditProgramDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_program_list.*
import java.time.LocalDateTime

class ProgramListFragment :
    Fragment(),
    EditProgramDialog.OnUpdated,
    ProgramListAdapter.OnItemSelected
{

    private lateinit var mContext: Context
    private lateinit var programViewModel: ProgramViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ProgramListAdapter(mContext)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(context)
        programViewModel = ViewModelProvider(this).get(ProgramViewModel::class.java)
        programViewModel.allPrograms.observe(viewLifecycleOwner, Observer { programs ->
            programs?.let { adapter.setPrograms(it) }
        })
        fab.setOnClickListener{
            val newProgram = TrainingProgram("", "#FF0000")
            val dialog = EditProgramDialog(newProgram, this)
            dialog.show(requireActivity().supportFragmentManager, "edit_program_dialog")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_program_list, container, false)
    }

    companion object{
        @JvmStatic
        fun newInstance() =
            ProgramListFragment()
    }

    override fun onUpdated(program: TrainingProgram) {
        if(program.programId == 0)
            programViewModel.insertProgram(program)
        else programViewModel.updateProgram(program)
    }

    override fun onItemSelected(program: TrainingProgramDetails) {
        val detailsFragment = ProgramDetailsFragment.newInstance(program)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_layout, detailsFragment, "detailsFragment")
            .addToBackStack(null)
            .commit()
    }

    override fun onDeleteItemSelected(program: TrainingProgramDetails) {
        programViewModel.deleteProgram(program.trainingProgram)
    }

    override fun onEditItemSelected(program: TrainingProgramDetails) {
        val dialog = EditProgramDialog(program.trainingProgram, this)
        dialog.show(requireActivity().supportFragmentManager, "edit_program_dialog")
    }
}