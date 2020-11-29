package com.example.first

import android.content.ClipData
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.text_layout.*

class TextFragmentPremium : TextFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonEditCopy.setOnClickListener(this)
        buttonTextCopy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            buttonTextCopy -> {
                val clip: ClipData = ClipData.newPlainText("textField", textView.text.toString())
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity, textView.text.toString() + " copied", Toast.LENGTH_SHORT)
                    .show()
            }
            buttonEditCopy -> {
                val clip: ClipData =
                    ClipData.newPlainText("editField", noImeEditText.text.toString())
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity, noImeEditText.text.toString() + " copied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun swap(){
        val temp = spinnerFirstUnits.adapter
        spinnerFirstUnits.adapter = spinnerSecondUnits.adapter
        spinnerSecondUnits.adapter = temp
        adapters[0]?.notifyDataSetChanged()
        adapters[1]?.notifyDataSetChanged()
    }
}