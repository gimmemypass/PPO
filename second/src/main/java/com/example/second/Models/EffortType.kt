package com.example.second.Models

import android.content.Context
import android.graphics.Color.green
import android.graphics.Color.red
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.second.R
import java.io.Serializable

@Entity
data class EffortType(
    @PrimaryKey var effortTypeId: Int,
    val type: String,
    val color: String
) : Serializable
//{
//    REST(R.string.effort_rest, R.color.green),
//    HARD(R.string.effor_hard, R.color.red),
//    CALM(R.string.effor_calm, R.color.purple),
//    NONE(R.string.effort_none, R.color.white);
//
//    public fun getStringId(context: Context): String {
//        return context.getString(stringId)
//    }
//    public fun getColorId(context: Context) : Int{
//        return context.resources.getColor(colorId)
//    }
//}