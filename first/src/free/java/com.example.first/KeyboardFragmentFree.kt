package com.example.first

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.button0
import kotlinx.android.synthetic.main.activity_main.button1
import kotlinx.android.synthetic.main.activity_main.button2
import kotlinx.android.synthetic.main.activity_main.button3
import kotlinx.android.synthetic.main.activity_main.button4
import kotlinx.android.synthetic.main.activity_main.button5
import kotlinx.android.synthetic.main.activity_main.button6
import kotlinx.android.synthetic.main.activity_main.button7
import kotlinx.android.synthetic.main.activity_main.button8
import kotlinx.android.synthetic.main.activity_main.button9
import kotlinx.android.synthetic.main.activity_main.buttonDelete
import kotlinx.android.synthetic.main.activity_main.buttonTranslate
import kotlinx.android.synthetic.main.keyboard.*

class KeyboardFragmentFree : KeyboardFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View?) {
        when(v){
            button0, button1, button2,
            button3, button4, button5,
            button6, button7, button8,
            button9, buttonDot -> {
                viewModel.appendEdit(v?.tag.toString())
            }
            buttonTranslate -> funListener?.OnTranslateSelected()
            buttonDelete -> viewModel.deleteCharacterEdit()
        }
    }
}