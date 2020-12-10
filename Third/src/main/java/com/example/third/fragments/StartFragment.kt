package com.example.third.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.example.third.GameViewModel
import com.example.third.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class StartFragment : Fragment() {
    private lateinit var fab : FloatingActionButton
    private lateinit var joinButton : Button
    private lateinit var idEditText : EditText
    private lateinit var userPageButton : Button

    private val gameViewModel : GameViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)
        fab = view.findViewById(R.id.start_floatingActionButton)
        joinButton = view.findViewById(R.id.start_join_button)
        idEditText = view.findViewById(R.id.start_id_editText)
        userPageButton = view.findViewById(R.id.start_userPageButton)
        joinButton.setOnClickListener{
            gameViewModel.joinGame(idEditText.text.toString())
        }
        fab.setOnClickListener{
            val result = gameViewModel.createGame()
            if(result){
                createWaitingFragment(gameViewModel.getGameId()!!)
            }
        }
        userPageButton.setOnClickListener {
            createUserPageFragment()
        }
        return view
    }

    private fun createUserPageFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.root_layout, UserFragment.newInstance(), "userFragment")
            .addToBackStack(null)
            .commit()
    }


    private fun createWaitingFragment(id : String){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.root_layout, WaitingFragment.newInstance(id), "waitingFragment")
            .addToBackStack(null)
            .commit()
    }
    companion object {
        @JvmStatic
        fun newInstance() =
                StartFragment()
    }
}