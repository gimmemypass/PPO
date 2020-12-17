package com.example.third.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.example.third.R
import com.example.third.UserViewModel
import com.google.android.gms.common.SignInButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthenticationFragment : Fragment() {
    private val TAG = "authentication"
    private lateinit var signInButton : SignInButton
    private lateinit var auth : FirebaseAuth
    private lateinit var rootLayout : ConstraintLayout

    private lateinit var gso : GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    private val userViewModel : UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_authentication, container, false)
        rootLayout = view.findViewById(R.id.authentication_root)
        signInButton = view.findViewById(R.id.authentication_sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            signIn()
        }
        return view
    }

    private fun signIn(){
        startActivityForResult(googleSignInClient.signInIntent, 1)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignIntResult(task)
        }
    }
    private fun handleSignIntResult(completedTask : Task<GoogleSignInAccount>){
        try{
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken!!)
        }
        catch (e : ApiException){
            Log.w(TAG, "signInResult: failed code = " + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        Toast.makeText(requireContext(),"Authentication is successful.\n Welcome, " + user?.displayName, Toast.LENGTH_SHORT ).show()
                        userViewModel.SignIn(user!!)
                        closeFragment()
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Snackbar.make(rootLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    }
                }
    }
    private fun closeFragment(){
        requireActivity().supportFragmentManager.popBackStack()
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            AuthenticationFragment()
    }
}