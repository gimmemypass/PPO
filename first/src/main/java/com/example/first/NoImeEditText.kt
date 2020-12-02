package com.example.first

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager


class NoImeEditText(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context!!, attrs) {

    override fun onCheckIsTextEditor(): Boolean {
        hideKeyboard()
        return super.onCheckIsTextEditor()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}