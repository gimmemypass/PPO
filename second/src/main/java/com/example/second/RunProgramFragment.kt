package com.example.second

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.ViewUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.second.data.RunProgramViewModel
import com.example.second.util.ViewUtils.Companion.getMinutesFromMs


class RunProgramFragment : Fragment() {
    private val viewModel : RunProgramViewModel by activityViewModels()
    private lateinit var currentTextView : TextView
    private lateinit var nextTextView : TextView
    private lateinit var timeRemainig : TextView
    private lateinit var playPauseButton : Button
    private lateinit var stopButton : Button
    private lateinit var progressBar : ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_run_program, container, false)
        currentTextView = view.findViewById(R.id.run_fragment_current_exercise_text_view)
        nextTextView = view.findViewById(R.id.run_fragment_next_exercise_text_view)
        playPauseButton = view.findViewById(R.id.run_fragment_play_button)
        stopButton = view.findViewById(R.id.run_fragment_stop_button)
        timeRemainig = view.findViewById(R.id.run_fragment_remaining_time_text_view)
        progressBar = view.findViewById(R.id.progressBar)
        playPauseButton.setOnClickListener{
            if(viewModel.isRunning()) {
                viewModel.pause()
                playPauseButton.text = getString(R.string.start)
            }
            else{
                viewModel.start()
                playPauseButton.text = getString(R.string.pause)
            }
        }
        stopButton.setOnClickListener{
            viewModel.stop()
            playPauseButton.text = getString(R.string.start)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getColorCurrent().observe(viewLifecycleOwner, Observer<String>{
            currentTextView.setBackgroundColor(Color.parseColor(it))
        })
        viewModel.getColorNext().observe(viewLifecycleOwner, Observer<String>{
            nextTextView.setBackgroundColor(Color.parseColor(it))
        })
        viewModel.getTextCurrent().observe(viewLifecycleOwner, Observer<String>{
            currentTextView.text = it
        })
        viewModel.getTextNext().observe(viewLifecycleOwner, Observer<String>{
            nextTextView.text = it
        })
        viewModel.getTimeRemaining().observe(viewLifecycleOwner, Observer<String>{
            timeRemainig.text = it
        })
        viewModel.getPercentTimeRemaining().observe(viewLifecycleOwner, Observer<Int>{
            progressBar.progress = it
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() : RunProgramFragment {
            return RunProgramFragment()
        }
    }
}