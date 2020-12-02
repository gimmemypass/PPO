package com.example.second.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.second.Models.TrainingProgram
import com.example.second.Models.TrainingProgramDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProgramViewModel(application: Application) : AndroidViewModel(application) {
    private val repo : ProgramRepository
    val allPrograms: LiveData<List<TrainingProgramDetails>>

    init {
        val db = AppDatabase.buildDatabase(application, viewModelScope)
        val programDao = db.programDao()
        repo = ProgramRepository(programDao)
        allPrograms = repo.allPrograms
    }

    fun insertProgram(program: TrainingProgram) = viewModelScope.launch(Dispatchers.IO){
        repo.insertProgram(program)
    }

    fun updateProgram(program: TrainingProgram) = viewModelScope.launch(Dispatchers.IO){
        repo.updateProgram(program)
    }
    fun deleteProgram(program: TrainingProgram) = viewModelScope.launch(Dispatchers.IO){
        repo.deleteProgram(program)
    }
    fun deleteAllPrograms() = viewModelScope.launch(Dispatchers.IO){
        repo.deleteAllPrograms()
    }
}
