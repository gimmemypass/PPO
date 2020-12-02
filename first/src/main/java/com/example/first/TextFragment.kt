package com.example.first

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.buttonDelete
import kotlinx.android.synthetic.main.activity_main.buttonTranslate
import kotlinx.android.synthetic.main.keyboard.*
import kotlinx.android.synthetic.main.text_layout.*
import kotlinx.android.synthetic.main.text_layout.noImeEditText

abstract class TextFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
   // private val model : SharedViewModel by activityViewModels()
    protected lateinit var clipboard : ClipboardManager
    protected lateinit var adapterCategory : ArrayAdapter<String>
    protected var adapters  = arrayOfNulls<ArrayAdapter<String>>(2)
    protected val viewModel: MyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.text_layout, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getEdit().observe(viewLifecycleOwner, Observer<String>{
            noImeEditText.setText(it)
        })
        viewModel.getText().observe(viewLifecycleOwner, Observer<String>{
            textView.text = it
        })
        setSpinners()
        clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    }
    override fun onClick(v: View?) {
    }

    private fun setSpinners(){
        adapterCategory =
            activity?.let { ArrayAdapter<String>(it, R.layout.spinner_item, viewModel.unitManager.getCategories()) }!!
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapterCategory
        spinnerCategory.prompt = "Category"
        spinnerCategory.setSelection(0)
        spinnerCategory.onItemSelectedListener = this
//        adapterFirst = ArrayAdapter<String>(this, R.layout.spinner_item, unitManager.getCategoriesFirstUnits(spinnerCategory.selectedItemPosition))
        adapters[0] = activity?.let { ArrayAdapter<String>(it, R.layout.spinner_item, viewModel.unitManager.getCategoriesFirstUnits(spinnerCategory.selectedItemPosition)) }!!
        adapters[0]?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFirstUnits.adapter = adapters[0]
        spinnerFirstUnits.prompt = "First"
        spinnerFirstUnits.setSelection(0)
        spinnerFirstUnits.onItemSelectedListener = this
        adapters[1] = activity?.let { ArrayAdapter<String>(it, R.layout.spinner_item, viewModel.unitManager.getCategoriesSecondUnits(spinnerCategory.selectedItemPosition)) }!!
        adapters[1]?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSecondUnits.adapter = adapters[1]
        spinnerSecondUnits.prompt = "Second"
        spinnerSecondUnits.setSelection(0)
        spinnerSecondUnits.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            when(parent){
                spinnerCategory -> {
                    adapters[0]?.clear()
                    adapters[0]?.addAll(viewModel.unitManager.getCategoriesFirstUnits(position))
                    adapters[0]?.notifyDataSetChanged()
                    adapters[1]?.clear()
                    adapters[1]?.addAll(viewModel.unitManager.getCategoriesSecondUnits(position))
                    adapters[1]?.notifyDataSetChanged()
                }
            }
        }
    }

    open fun swap(){

    }
    public fun translate(){
        var value : Double = 0.0
        val text = noImeEditText.text.toString()
        if(text.isNotEmpty()){
            value = text.toDouble()
        }
        viewModel.setText(viewModel.unitManager.translate(value,
                                              spinnerFirstUnits.selectedItem.toString(),
                                              spinnerSecondUnits.selectedItem.toString(),
                                              spinnerCategory.selectedItemPosition).toString())
    }

    public fun addCharacter(char : String){
        viewModel.appendEdit(char)
    }
}