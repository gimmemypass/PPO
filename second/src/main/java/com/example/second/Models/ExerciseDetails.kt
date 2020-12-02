package com.example.second.Models

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class ExerciseDetails (
//    @Embedded val effortType: EffortType,
//    @Relation(
//        parentColumn = "effortTypeId",
//        entityColumn = "effortTypeFk"
//    )
//    val exercise : Exercise
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "effortTypeFk",
        entityColumn = "effortTypeId"
    )
    val effortType: EffortType
) : Serializable