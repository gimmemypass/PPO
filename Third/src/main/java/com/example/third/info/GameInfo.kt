package com.example.third.info

data class GameInfo (
    var player1Id : String = "",
    var player2Id : String = "",
    var turnPlayerId : String = "",
    var winnerId : String = "",
    var field : MutableList<MutableList<String>> = mutableListOf()
)