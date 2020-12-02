package com.example.second.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.second.Models.TrainingProgram
import com.example.second.Models.TrainingProgramDetails

@Dao
interface ProgramDAO {
    @Transaction
    @Query("SELECT * FROM TrainingProgram")
    fun getPrograms() : LiveData<List<TrainingProgramDetails>>

    @Query("SELECT * FROM TrainingProgram WHERE programId = :id")
    fun getProgram(id : Int) : LiveData<TrainingProgram>

    @Query("SELECT * FROM TrainingProgram WHERE programId = :id")
    fun getProgramSync(id : Int) : TrainingProgram

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgram(program: TrainingProgram) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertManyPrograms(programs : List<TrainingProgram>) : List<Long>

    @Update
    fun updateProgram(program: TrainingProgram)

    @Update
    fun updateManyPrograms(programs: List<TrainingProgram>) : Int

    @Delete
    fun deleteProgram(program: TrainingProgram)

    @Delete
    fun deleteManyPrograms(programs: List<TrainingProgram>) : Int

    @Query("DELETE FROM TrainingProgram")
    fun deleteAll()
}