package com.example.third

import android.util.Log
import com.example.third.info.RequestInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class OwnerGameController(
    gameId : String,
    listener : OnDataChanged
) : GameController(gameId, listener)
{
    override val symbol: String
        get() = "X"
    private val dbRefTurn = dbRefGame.child("turnPlayerId")
    private var field = MutableList(3) {MutableList(3) {""} }

    private var enemyUID : String = ""
    init {
        addValueEventListener()
    }


    override fun act(rowCol : String) {
        if(!ableToAct)
            return
        val row = rowCol[0].toInt() - '0'.toInt()
        val col = rowCol[1].toInt() - '0'.toInt()
        if(checkCellIsEmpty(row, col)){
            markCell(row, col, symbol)
            changeTurn(enemyUID)
        }
    }

    override fun exit() {
        dbRefGame.child("player1Id").setValue("")
    }

    private fun startGame(){
        listener.onPlayer2Connected()
        chooseFirstPlayer()
    }

    private fun chooseFirstPlayer() {
        if(Random.nextBoolean()){
            dbRefTurn.setValue(uid)
        }
        else{
            dbRefTurn.setValue(enemyUID)
        }
    }

    private fun checkGameState(){
        dbRefGame.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val hashMap = snapshot.value as HashMap<*,*>
                val field = hashMap["field"] as MutableList<MutableList<String>>
                if(field[0][0].isNotEmpty() && field[0][0] == field[0][1] && field[0][1] == field[0][2]) setWinner(field[0][0])
                if(field[1][0].isNotEmpty() && field[1][0] == field[1][1] && field[1][1] == field[1][2]) setWinner(field[1][0])
                if(field[2][0].isNotEmpty() && field[2][0] == field[2][1] && field[2][1] == field[2][2]) setWinner(field[2][0])
                if(field[0][0].isNotEmpty() && field[0][0] == field[1][0] && field[1][0] == field[2][0]) setWinner(field[0][0])
                if(field[0][1].isNotEmpty() && field[0][1] == field[1][1] && field[1][1] == field[2][1]) setWinner(field[0][1])
                if(field[0][2].isNotEmpty() && field[0][2] == field[1][2] && field[1][2] == field[2][2]) setWinner(field[0][2])
                if(field[0][0].isNotEmpty() && field[0][0] == field[1][1] && field[1][1] == field[2][2]) setWinner(field[0][0])
                if(field[0][2].isNotEmpty() && field[0][2] == field[1][1] && field[1][1] == field[2][0]) setWinner(field[0][2])
            }

        })
    }
    private fun setWinner(sym: String){
        if(symbol == sym) dbRefGame.child("winnerId").setValue(uid)
        else dbRefGame.child("winnerId").setValue(enemyUID)
        field = MutableList(3) {MutableList(3) {""} }
        dbRefGame.child("field").setValue(field)
        listener.onFieldChanged(field)
    }

    private fun checkCellIsEmpty(row : Int, col : Int) =  field[row][col].isEmpty()

    private fun markCell(row : Int, col : Int, symbol : String){
        field[row][col] = symbol
        listener.onCellChanged(row, col, symbol)
        dbRefGame.child("field").setValue(field)
        checkGameState()
    }
    private fun changeTurn(uid : String){
        dbRefTurn.setValue(uid)
    }
    private fun addValueEventListener(){
        val requestListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value == null)
                    return
                val map = snapshot.value as HashMap<*,*>
                val request = RequestInfo()
                request.col = map["col"].toString().toInt()
                request.row = map["row"].toString().toInt()
                request.symbol = map["symbol"].toString()
                request.uid = map["uid"].toString()
                if(checkCellIsEmpty(request.row, request.col)){
                    markCell(request.row, request.col, request.symbol)
                    changeTurn(uid)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("GameFragment", "onCancelled", error.toException())
            }
        }
        dbRefRequest.addValueEventListener(requestListener)
        //request ref
        val turnListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ableToAct = snapshot.value as String == Firebase.auth.currentUser?.uid
                listener.onTurnChanged(ableToAct)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("GameFragment", "onCancelled", error.toException())
            }
        }
        dbRefTurn.addValueEventListener(turnListener)

        val enemyUIDlistener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
//                enemyUID = snapshot.value.toString()
                if(snapshot.value.toString().isNotEmpty()){
                    enemyUID = snapshot.value.toString()
                    startGame()
                }
                else if(enemyUID.isNotEmpty()){
                    listener.onOpponentDisconnected()
                    enemyUID = ""
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("GameFragment", "onCancelled", error.toException())
            }
        }
        dbRefGame.child("player2Id").addValueEventListener(enemyUIDlistener)
        dbRefGame.child("winnerId").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val winnerId = snapshot.value.toString()
                if(winnerId.isNotEmpty()){
                    listener.onGameOver(winnerId == uid)
                    dbRefGame.child("winnerId").setValue("")
                }
            }

        })
    }
}