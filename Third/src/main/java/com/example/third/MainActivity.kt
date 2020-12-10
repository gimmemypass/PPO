package com.example.third

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.third.fragments.AuthenticationFragment
import com.example.third.fragments.GameFragment
import com.example.third.fragments.StartFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val gameViewModel : GameViewModel by viewModels()
    private val userViewModel : UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            createStartFragment()
            val user = Firebase.auth.currentUser
            if(user == null){
                createAuthenticationFragment()
            }
            else{
                userViewModel.SignIn(user)
            }
        }
        gameViewModel.eventCreateGameFragment.observe(this, Observer{ event ->
            event?.getContentIfNotHandledOrReturnNull()?.let{
                if(it)
                    supportFragmentManager.popBackStack()
                createGameFragment()
            }
        })
    }

    override fun onBackPressed() {
        val authFragment = supportFragmentManager.findFragmentByTag("authenticationFragment")
        if(authFragment != null && authFragment.isVisible){
            Toast.makeText(this, "You must sign in to play", Toast.LENGTH_SHORT).show()
        }
        else{
            super.onBackPressed()
        }
    }
    private fun createAuthenticationFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_layout, AuthenticationFragment.newInstance(), "authenticationFragment" )
            .addToBackStack(null)
            .commit()
    }
    private fun createStartFragment(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.root_layout, StartFragment.newInstance(), "startFragment")
            .commit()
    }
    private fun createGameFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.root_layout, GameFragment.newInstance(), "gameFragment")
            .addToBackStack(null)
            .commit()
    }
}