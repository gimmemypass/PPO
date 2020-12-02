package com.example.second.Models

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.SET_DEFAULT
import java.io.Serializable

@Entity(foreignKeys = arrayOf(
    ForeignKey(
        entity = TrainingProgram::class,
        parentColumns = ["programId"],
        childColumns = ["programFk"],
        onDelete = CASCADE
    ),
    ForeignKey(
        entity = EffortType::class,
        parentColumns = ["effortTypeId"],
        childColumns = ["effortTypeFk"],
        onDelete = SET_DEFAULT
    )
))

data class Exercise(

    var programFk: Int,

    @ColumnInfo(defaultValue = "0")
    var effortTypeFk: Int,
    var name: String,
    var duration: Int

) : Serializable
{
    @PrimaryKey(autoGenerate = true) var exerciseId: Int = 0
}