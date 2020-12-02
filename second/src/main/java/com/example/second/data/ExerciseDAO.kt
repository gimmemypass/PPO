package com.example.second.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.second.Models.*

@Dao
interface ExerciseDAO {
    @Transaction
    @Query("SELECT * FROM Exercise WHERE programFk = :programId")
    fun getExercises(programId : Int) : LiveData<List<ExerciseDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(exercise: Exercise) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertManyExercises(exercises: List<Exercise>) : List<Long>

    @Update
    fun updateExercise(exercise: Exercise)

    @Delete
    fun deleteExercise(exercise: Exercise)

    @Delete
    fun deleteManyExercises(exercises : List<Exercise>) : Int

    @Query("DELETE FROM Exercise")
    fun deleteAll()

    ////////////////effort type
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEffortType(effortType: EffortType)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertManyEffortType(effortTypes: List<EffortType>)

    @Query("SELECT * FROM EffortType")
    fun getEffortTypes() : List<EffortType>

    @Query("DELETE FROM EffortType")
    fun deleteEffortTypes()
}