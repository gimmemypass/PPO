package com.example.second.util

import android.widget.TextView

class ViewUtils {
    companion object{
        fun getIntFromTextViewSafe(view: TextView) : Int{
            if(view.text == null || view.text.toString().isEmpty()){
                return 0
            }
            else
                return view.text.toString().toInt()
        }

        fun getMinutesFromMs(ms : Int) = ms / 1000 / 60
        fun getSecondsFromMs(ms : Int) = ms / 1000 % 60

        fun stringColorFromInt(int : Int) : String{
            return String.format("#%06X", (0xFFFFFF and int))
        }

    }
}