package com.example.second

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.second.data.ProgramViewModel
import com.example.second.util.SharedPref
import java.util.*

class SettingsFragment : Fragment() {
    public interface SettingsInterface {
        fun onFontScaleChanged()
        fun onNightModeChanged()
        fun onLanguageChanged()
    }
    private lateinit var darkModeSwitch : Switch
    private lateinit var increaseFontSizeButton : Button
    private lateinit var decreaseFontSizeButton : Button
    private lateinit var enLocaleButton : Button
    private lateinit var ruLocaleButton : Button
    private lateinit var clearDataButton : Button

    private lateinit var prefs : SharedPref
    private lateinit var listener : SettingsInterface
    private lateinit var programViewModel : ProgramViewModel

    private val DELTA_SCALE : Float = 0.1F

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is SettingsInterface){
            listener = context
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefs = SharedPref(requireContext())
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        programViewModel = ViewModelProvider(this).get(ProgramViewModel::class.java)
        darkModeSwitch = view.findViewById(R.id.setting_dark_mode_switch)
        increaseFontSizeButton = view.findViewById(R.id.setting_increase_font_size)
        decreaseFontSizeButton = view.findViewById(R.id.setting_decrease_font_size)
        enLocaleButton = view.findViewById(R.id.setting_en_locale_button)
        ruLocaleButton = view.findViewById(R.id.setting_ru_locale_button)
        clearDataButton = view.findViewById(R.id.setting_clear_data_button)

        darkModeSwitch.isChecked = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        darkModeSwitch.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                prefs.setNightModeState(true)
                listener.onNightModeChanged()
            }
            else{
                prefs.setNightModeState(false)
                listener.onNightModeChanged()
            }
        }
        increaseFontSizeButton.setOnClickListener{
            scaleFont(DELTA_SCALE)
        }
        decreaseFontSizeButton.setOnClickListener{
            scaleFont(-DELTA_SCALE)
        }
        enLocaleButton.setOnClickListener{
            setLocale("en")
        }
        ruLocaleButton.setOnClickListener{
            setLocale("ru")
        }
        clearDataButton.setOnClickListener {
            clearData()
        }
        return view
    }
    private fun scaleFont(delta: Float){
        val fontScale = prefs.getFontScale() + delta
        prefs.setFontScale(fontScale)
        listener.onFontScaleChanged()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }
    private fun setLocale(code: String) {
        prefs.setLocale(code)
        listener.onLanguageChanged()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }
    private fun clearData(){
        programViewModel.deleteAllPrograms()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment()
    }
}