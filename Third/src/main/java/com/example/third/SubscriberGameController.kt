package com.example.third

import android.util.Log
import com.example.third.info.GameInfo
import com.example.third.info.RequestInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SubscriberGameController(
    id : String,
    listener : GameController.OnDataChanged
) : GameController(id,listener) {
    init {
        addValueEventListener()
        dbRefGame.child("player2Id").setValue(uid)
    }
    override val symbol: String
        get() = "O"

    override fun act(rowCol : String) {
        if(ableToAct){
            val requestInfo = RequestInfo(
                    rowCol[0].toInt() - '0'.toInt(),
                    rowCol[1].toInt() - '0'.toInt(),
                    symbol,
                    uid
            )
            dbRefRequest.setValue(requestInfo)
        }
    }

    override fun exit() {
        dbRefGame.child("player2Id").setValue("")
    }

    private fun addValueEventListener(){
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hashMap = snapshot.value as HashMap<*,*>
                val info : GameInfo = GameInfo()
                info.player1Id = hashMap["player1Id"].toString()
                info.turnPlayerId = hashMap["turnPlayerId"].toString()
                info.winnerId = hashMap["winnerId"].toString()
                info.field = hashMap["field"] as MutableList<MutableList<String>>
                listener.onFieldChanged(info.field)
                ableToAct = (uid == info.turnPlayerId)
                listener.onTurnChanged(ableToAct)
                if(info.winnerId.isNotEmpty()){
                    listener.onGameOver(info.winnerId == uid)
                }
                if(info.player1Id.isEmpty()){
                    listener.onOpponentDisconnected()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("GameFragment", "onCancelled", error.toException())
            }
        }
        dbRefGame.addValueEventListener(listener)
    }
}