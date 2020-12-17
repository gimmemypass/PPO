package com.example.third

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

abstract class GameController(
    protected val gameId: String,
    protected val listener: OnDataChanged
) {
    interface OnDataChanged{
        fun onCellChanged(row : Int, col: Int, symbol : String)
        fun onFieldChanged(field : MutableList<MutableList<String>>)
        fun onPlayer2Connected()
        fun onTurnChanged(isYourTurn : Boolean)
        fun onGameOver(hasWon : Boolean)
        fun onOpponentDisconnected()
    }
    protected val dbRefGame : DatabaseReference = FirebaseDatabase.getInstance().getReference("games/$gameId/game")
    protected val dbRefRequest : DatabaseReference = FirebaseDatabase.getInstance().getReference("games/$gameId/request")
    protected abstract val symbol : String
    protected var ableToAct : Boolean = false
    protected val uid : String = Firebase.auth.currentUser?.uid!!

    public abstract fun act(rowCol : String)
    public abstract fun exit()
}