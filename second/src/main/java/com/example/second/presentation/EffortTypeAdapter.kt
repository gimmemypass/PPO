package com.example.second.presentation

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColorInt
import com.example.second.R
import com.example.second.data.ExerciseDAO

class EffortTypeAdapter(
    context: Context,
    private val exerciseDAO: ExerciseDAO
) : BaseAdapter() {
    private val effortTypes = exerciseDAO.getEffortTypes()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = convertView
        if(view == null)
            view = inflater.inflate(R.layout.spinner_effort_type, null)
        val effort = effortTypes[position]
        val textView = view?.findViewById<TextView>(R.id.text_effort_type)
        textView?.text = effort.type
        val colorView = view?.findViewById<View>(R.id.color_effort_type)
        colorView?.setBackgroundColor(Color.parseColor(effort.color))
        return view!!
    }

    override fun getItem(position: Int): Any = effortTypes[position]

    override fun getItemId(position: Int): Long =  position.toLong()

    override fun getCount(): Int = effortTypes.size
}