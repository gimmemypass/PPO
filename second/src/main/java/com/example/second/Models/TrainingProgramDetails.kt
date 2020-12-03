package com.example.second.Models

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class TrainingProgramDetails(
    @Embedded val trainingProgram : TrainingProgram,
    @Relation(
        parentColumn = "programId",
        entityColumn = "programFk",
        entity = Exercise::class
    )
    val exercises: List<ExerciseDetails>
) : Serializable

