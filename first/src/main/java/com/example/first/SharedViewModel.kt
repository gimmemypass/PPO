package com.example.first

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var selected = MutableLiveData<String>()

    fun select(text : String){
        selected.value = text
    }
}