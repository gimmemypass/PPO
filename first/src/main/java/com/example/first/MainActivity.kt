package com.example.first

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class MainActivity : AppCompatActivity(), KeyboardFragment.OnFunctionSelectedListener {
    private var keyFragment: KeyboardFragment? = null
    private var textFragment : TextFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        keyFragment = supportFragmentManager.findFragmentByTag("keyboard") as KeyboardFragment?
        textFragment = supportFragmentManager.findFragmentByTag("text") as TextFragment?
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }

    override fun OnSwapSelected() {
        textFragment?.swap()
    }

    override fun OnTranslateSelected() {
        textFragment?.translate()
    }

}



