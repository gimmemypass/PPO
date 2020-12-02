package com.example.second

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.second.Models.TrainingProgram
import com.example.second.Models.TrainingProgramDetails
import com.example.second.data.AppDatabase
import com.example.second.data.ProgramViewModel
import com.example.second.presentation.EditProgramDialog
import com.example.second.util.SharedPref
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import java.util.*

class MainActivity :
    AppCompatActivity(),
    ColorPickerDialogListener,
    SettingsFragment.SettingsInterface{
    private lateinit var programViewModel: ProgramViewModel
    private val newProgramActivityRequestCode = 1
    private lateinit var fragment : ProgramListFragment
    private lateinit var prefs : SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = SharedPref(this)
        setSettings();
        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.root_layout, ProgramListFragment.newInstance(), "programListFragment")
                .commit()
        }
    }

    private fun setSettings(){
        onFontScaleChanged()
        onNightModeChanged()
        onLanguageChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_settings -> {
                val settingsFragment = SettingsFragment.newInstance()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.root_layout, settingsFragment, "settingsFragment")
                    .addToBackStack(null)
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDialogDismissed(dialogId: Int) {
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        val fragment = supportFragmentManager.findFragmentByTag("edit_program_dialog") as EditProgramDialog
        fragment.onColorSelected(color)
    }

    override fun onFontScaleChanged() {
        val scale = prefs.getFontScale()
        resources.configuration.fontScale = scale
        val metrics = resources.displayMetrics
        metrics.scaledDensity = resources.configuration.fontScale * metrics.density
    }

    override fun onNightModeChanged() {
        if(prefs.getNightModeState()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onLanguageChanged() {
        val code = prefs.getLocale()
        val locale = Locale(code)
        val config = baseContext.resources.configuration
        Locale.setDefault(locale)
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }

}

