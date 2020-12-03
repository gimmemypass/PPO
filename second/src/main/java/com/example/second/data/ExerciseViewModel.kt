package com.example.second.data

import android.app.Application
import androidx.lifecycle.*
import com.example.second.Models.Exercise
import com.example.second.Models.ExerciseDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    val mutableProgramId: MutableLiveData<Int> = MutableLiveData()
    private val db = AppDatabase.buildDatabase(application, viewModelScope)
    private val exerciseDao = db.exerciseDao()

    val exercises : LiveData<List<ExerciseDetails>> = Transformations.switchMap(mutableProgramId){
        exerciseDao.getExercises(it)
    }

    fun searchExercisesByProgramId(id : Int){
        mutableProgramId.value = id
    }

    fun insertExercise(exercise: Exercise) = viewModelScope.launch(Dispatchers.IO) {
            exerciseDao.insertExercise(exercise).toInt()
    }

    fun updateExercise(exercise: Exercise) = viewModelScope.launch(Dispatchers.IO){
        exerciseDao.updateExercise(exercise)
    }

    fun deleteExercise(exercise: Exercise) = viewModelScope.launch(Dispatchers.IO) {
        exerciseDao.deleteExercise(exercise)
    }

}