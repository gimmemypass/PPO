package com.example.third

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class UserGamesListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<UserGamesListAdapter.UserGamesViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var games = emptyList<Boolean>()

    inner class UserGamesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val resultItem : TextView = itemView.findViewById(R.id.resultTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserGamesViewHolder {
        val itemView = inflater.inflate(R.layout.recycler_item_user_game, parent, false)
        return UserGamesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return games.count()
    }

    override fun onBindViewHolder(holder: UserGamesViewHolder, position: Int) {
        val current = games[position]
        if(current){
            holder.resultItem.text = "Victory"
        }
        else{
            holder.resultItem.text = "Defeat"
        }
    }
    internal fun setGames(games : List<Boolean>){
        this.games = games
        notifyDataSetChanged()
    }
}