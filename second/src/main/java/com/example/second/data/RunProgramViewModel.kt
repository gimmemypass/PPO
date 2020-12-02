package com.example.second.data

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.*
import com.example.second.Models.ExerciseDetails
import com.example.second.util.ViewUtils

class RunProgramViewModel(application: Application) : AndroidViewModel(application) {
    private var _currentText : MutableLiveData<String> = MutableLiveData("")
    private var _currentColor : MutableLiveData<String> = MutableLiveData("#FFFFFF")
    private var _nextText : MutableLiveData<String> = MutableLiveData("")
    private var _nextColor : MutableLiveData<String> = MutableLiveData("#FFFFFF")
    private var _timeRemaining : MutableLiveData<String> = MutableLiveData("10:10")
    private var _timePercentRemaining : MutableLiveData<Int> = MutableLiveData(0)

    private var exercises = emptyList<ExerciseDetails>()
    private var curExerciseInd = 0
    private lateinit var countDownTimer: CountDownTimer
    private var _msProgramRemaining : Long = 0
    private var _msExerciseRemaining : Long = 0
    private var isRunning : Boolean = false
    private val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()
    private val soundPool = SoundPool.Builder()
        .setAudioAttributes(attributes)
        .build()

    init{

    }
    public fun loadSound(res : Int ){
        soundPool.load(getApplication<Application>().applicationContext, res, 1)
    }
    fun getTextCurrent() = _currentText
    fun getColorCurrent() = _currentColor
    fun getTextNext() = _nextText
    fun getColorNext() = _nextColor
    fun getTimeRemaining() = _timeRemaining
    fun getPercentTimeRemaining() = _timePercentRemaining
    fun isRunning()  = isRunning

    private fun setTimeRemaining(ms: Int){
        _timeRemaining.value = ViewUtils.getMinutesFromMs(ms).toString() + ":" + ViewUtils.getSecondsFromMs(ms).toString()
        if(curExerciseInd < exercises.size)
            _timePercentRemaining.value = 100 * ms / exercises[curExerciseInd].exercise.duration
        else _timePercentRemaining.value = 0
    }
    public fun setUpFragment(){
        setCurrent(0)
        setNext(1)
        _timePercentRemaining.value = 100
        _timeRemaining.value = "0:0"
        this._msProgramRemaining = exercises.sumBy { it.exercise.duration }.toLong()

        if(exercises.isNotEmpty())
            this._msExerciseRemaining = exercises[0].exercise.duration.toLong()
        else
            this._msExerciseRemaining = 0
    }

    private fun setTable(){
        setCurrent(curExerciseInd)
        setNext(curExerciseInd + 1)

    }


    private fun setCurrent(id : Int){
        if(exercises.count() > id){
            _currentColor.value = exercises[id].effortType.color
            _currentText.value = exercises[id].exercise.name
        }
        else{
            _currentColor.value = "#FFFFFF"
            _currentText.value = ""
        }

    }

    private fun setNext(id : Int){
        if(exercises.count() > id){
            _nextColor.value = exercises[id].effortType.color
            _nextText.value = exercises[id].exercise.name

        }else
        {
            _nextColor.value = "#FFFFFF"
            _nextText.value = ""
        }
    }
    fun setExercises(exercises: List<ExerciseDetails>){
        this.exercises = exercises
    }

    fun pause(){
        isRunning = false
        countDownTimer.cancel()
    }

    fun start(){
        isRunning = true
        //start timer
        val interval : Long = 1000
//        _msExerciseRemaining = exercises[curExerciseInd].exercise.duration.toLong()
        countDownTimer = object : CountDownTimer(_msProgramRemaining, interval){
            override fun onFinish() {
                soundPool.play(1, 1F, 1F, 1,0, 1F)
                stop()
            }
            override fun onTick(millisUntilFinished: Long) {
                _msExerciseRemaining -= interval
                if(_msExerciseRemaining <= 0){
                    soundPool.play(1, 1F, 1F, 1,0, 1F)
                    Toast.makeText(getApplication<Application>().applicationContext, "sound", Toast.LENGTH_SHORT).show()
                    curExerciseInd++
                    if(exercises.size > curExerciseInd){
                        setTable()
                        _msExerciseRemaining += exercises[curExerciseInd].exercise.duration.toLong()
                    }
                    else{
                        _msExerciseRemaining = 0
                    }
                }
                _msProgramRemaining = millisUntilFinished
                setTimeRemaining(_msExerciseRemaining.toInt())

            }

        }
        countDownTimer.start()
    }
    fun stop(){
        isRunning = false
        countDownTimer.cancel()
        curExerciseInd = 0
        setUpFragment()
    }
}