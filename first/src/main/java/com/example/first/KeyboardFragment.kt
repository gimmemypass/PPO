package com.example.first

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

abstract class KeyboardFragment : Fragment(), View.OnClickListener {
    public interface OnFunctionSelectedListener{
        public fun OnSwapSelected()
        public fun OnTranslateSelected()
    }
    protected val viewModel : MyViewModel by activityViewModels()

    protected var funListener : OnFunctionSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if ( context is OnFunctionSelectedListener)
            funListener = context
        else
            throw RuntimeException(context.toString() + "must implement OnFunctionSelectedListener")
    }

    override fun onDetach() {
        super.onDetach()
        funListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.keyboard, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val numberBtn = arrayOfNulls<Button>(10)
        numberBtn[0] = button0
        numberBtn[1] = button1
        numberBtn[2] = button2
        numberBtn[3] = button3
        numberBtn[4] = button4
        numberBtn[5] = button5
        numberBtn[6] = button6
        numberBtn[7] = button7
        numberBtn[8] = button8
        numberBtn[9] = button9
        for (i in numberBtn.indices) {
            numberBtn[i]?.setOnClickListener(this)
        }
        buttonDot.setOnClickListener(this)
        buttonDelete.setOnClickListener(this)
        buttonTranslate.setOnClickListener(this)
    }

}