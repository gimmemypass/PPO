package com.example.third.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.third.R


class WaitingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var id_param: String? = null
    private val ARG_PARAM_ID = "param_id"
    private lateinit var idTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id_param = it.getString(ARG_PARAM_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_waiting, container, false)
        idTextView = view.findViewById(R.id.waiting_idTextView)
        idTextView.text = id_param
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param_id: String) =
            WaitingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_ID, param_id)
                }
            }
    }
}