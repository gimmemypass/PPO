package com.example.second.util

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

class SharedPref(private val context: Context) {
    private val PREFSNAME = "prefs"
    private val NIGHTMODE = "NightMode"
    private val FONTSCALE = "FontScale"
    private val LOCALE = "Locale"

    private val pref = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    public fun setNightModeState(state : Boolean){
        editor.putBoolean(NIGHTMODE, state)
        editor.commit()
    }
    public fun getNightModeState(): Boolean {
        return pref.getBoolean(NIGHTMODE, false)
    }

    public fun setFontScale(scale : Float){
        editor.putFloat(FONTSCALE, scale)
        editor.commit()
    }
    public fun getFontScale() : Float{
        return pref.getFloat(FONTSCALE, 1.0F)
    }

    public fun setLocale(code : String){
        editor.putString(LOCALE, code)
        editor.commit()
    }
    public fun getLocale() : String {
        return pref.getString(LOCALE, "en")?: "en"
    }

}