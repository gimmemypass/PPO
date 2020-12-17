package com.example.third

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProviders
import com.example.third.info.GameInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class GameViewModel(application : Application) : AndroidViewModel(application), GameController.OnDataChanged{
    private var dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("games")
    private lateinit var userViewModel: UserViewModel
    private lateinit var gameController : GameController
    private var gameId : String? = ""
    var field : MutableLiveData<MutableList<MutableList<String>>> = MutableLiveData(MutableList(3) {MutableList(3) {""} })
        private set
    var eventCreateGameFragment = MutableLiveData<Event<Boolean>>()
        private set
    var eventTurnChanged = MutableLiveData<Event<Boolean>>()
        private set
    var eventGameOver = MutableLiveData<Event<Boolean>>()
        private set


    public fun createGame() : Boolean{
        val field = MutableList(3) {MutableList(3) {""} }
        val player1Id = Firebase.auth.currentUser?.uid
        val game =
            GameInfo(player1Id!!, "", "", "", field)
        val newGame = dbRef.push()
        gameId = newGame.key
        newGame.child("game").setValue(game)
        gameController = OwnerGameController(gameId!!,this)
        return true
    }
    public fun joinGame(id : String){
        val game = dbRef.orderByKey().equalTo(id)
        val vm = this
        game.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    gameController = SubscriberGameController(id, vm)
                    eventCreateGameFragment.value = Event(false) //send event to start game fragment
                }
                else{
                    Toast.makeText(getApplication(), "id does not exits", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
    public fun cellClicked(tag : String){
        gameController.act(tag)
    }

    public fun getGameId() : String? = gameId
    public fun exit() {
        gameController.exit()
    }

    override fun onCellChanged(row: Int, col: Int, symbol: String) {
        if(field.value == null)
            throw Exception("field is null")
        val list = mutableListOf<MutableList<String>>()
        list.addAll(field.value!!)
        list[row][col] = symbol
        field.value = list
    }

    override fun onFieldChanged(field: MutableList<MutableList<String>>) {
        this.field.value = field
    }

    override fun onPlayer2Connected() {
        eventCreateGameFragment.value = Event(true) //send event to pop waiting fragment and start game fragment
    }

    override fun onTurnChanged(isYourTurn: Boolean) {
        eventTurnChanged.value = Event(isYourTurn)
    }

    override fun onGameOver(hasWon: Boolean) {
        val result : String = if(hasWon){
            "Victory :)"
        } else{
            "Defeat :("
        }
        eventGameOver.value = Event(hasWon)
        Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT).show()
    }

    override fun onOpponentDisconnected() {
        Toast.makeText(getApplication(), "The opponent has disconnected...", Toast.LENGTH_SHORT).show()
    }
}