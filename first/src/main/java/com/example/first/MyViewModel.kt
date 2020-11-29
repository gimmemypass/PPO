package com.example.first

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.text_layout.*

class MyViewModel : ViewModel() {
    private var _textView : MutableLiveData<String> = MutableLiveData("");
    private var _editView : MutableLiveData<String> = MutableLiveData("")
    public var unitManager : UnitManager = UnitManager()

    fun getEdit(): MutableLiveData<String> {
        return _editView
    }
    fun getText(): MutableLiveData<String> {
        return _textView
    }

    fun setEdit(text : String){
        _editView.value = text
    }
    fun setText(text : String){
        _textView.value = text
    }

    fun appendEdit(char : String){
        _editView.value += char
    }

    fun deleteCharacterEdit(){
        val length = _editView.value?.length
        if(length != null && length > 0){
            _editView.value = _editView.value?.substring(0, _editView.value!!.length - 1);
        }
    }


    init {
        var tempCat: UnitCategory = UnitCategory("distance", "mm", "in", 0.03937)
        tempCat.addFirstSetUnit("cm", 10.0)
        tempCat.addFirstSetUnit("m", 1000.0)
        tempCat.addSecondSetUnit("ft", 12.0)
        tempCat.addSecondSetUnit("yd", 36.0)
        unitManager.addCategory(tempCat)
        tempCat = UnitCategory("weight", "g", "oz", 0.0353)
        tempCat.addFirstSetUnit("kg", 1000.0)
        tempCat.addFirstSetUnit("mg", 0.001)
        tempCat.addSecondSetUnit("lb", 16.0)
        tempCat.addSecondSetUnit("stone", 224.0)
        unitManager.addCategory(tempCat)
        tempCat = UnitCategory("area", "sq cm", "sq in", 0.1550)
        tempCat.addFirstSetUnit("sq m", 10000.0)
        tempCat.addFirstSetUnit("sq mm", 0.01)
        tempCat.addSecondSetUnit("sq ft", 144.0)
        tempCat.addSecondSetUnit("sq yd", 1296.0)
        unitManager.addCategory(tempCat)
    }

}