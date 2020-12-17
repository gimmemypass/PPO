package com.example.third.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.third.GameViewModel
import com.example.third.R
import com.example.third.UserViewModel

class GameFragment : Fragment(), View.OnClickListener {
    private var buttons =
        Array(
            3
        ) { arrayOfNulls<Button>(3) }
    private lateinit var turnTextView : TextView
    private lateinit var exitButton: Button
    private val gameViewModel : GameViewModel by activityViewModels()
    private val userViewModel : UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        for(i in 0..2){
            for(j in 0..2){
                buttons[i][j] = view.findViewWithTag(i.toString() + j.toString())
                buttons[i][j]?.setOnClickListener(this)
            }
        }
        turnTextView = view.findViewById(R.id.game_turnTextView)
        exitButton = view.findViewById(R.id.game_exitButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel.field.observe(viewLifecycleOwner, Observer<MutableList<MutableList<String>>>{
            for(i in 0..2){
                for(j in 0..2){
                    buttons[i][j]?.text = it[i][j]
                }
            }
        })
        gameViewModel.eventTurnChanged.observe(viewLifecycleOwner, Observer{ event ->
            event?.getContentIfNotHandledOrReturnNull()?.let{
                if(it){ //isYourTurn
                    turnTextView.text = "Your Turn"
                }
                else{
                    turnTextView.text = "Opponent's turn"
                }
            }
        })
        gameViewModel.eventGameOver.observe(viewLifecycleOwner, Observer{ event ->
            event?.getContentIfNotHandledOrReturnNull()?.let{
                userViewModel.GameOver(it)
            }
        })
        exitButton.setOnClickListener {
            gameViewModel.exit()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
                GameFragment()
    }

    override fun onClick(v: View?) {
        when(v){
            buttons[0][0],buttons[0][1],buttons[0][2],
            buttons[1][0],buttons[1][1],buttons[1][2],
            buttons[2][0],buttons[2][1],buttons[2][2] ->{
                gameViewModel.cellClicked(v?.tag.toString())
            }
        }
    }
}