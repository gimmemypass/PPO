package com.example.third

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.third.info.UserInfo
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class UserViewModel(application: Application) : AndroidViewModel(application){
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")
    private val storageRef = FirebaseStorage.getInstance().reference
    private lateinit var userInfo : UserInfo

    var name : MutableLiveData<String> = MutableLiveData("")
        private set

    public fun SignIn(user: FirebaseUser){
        val checkIfExists = dbRef.orderByKey().equalTo(user.uid)
        checkIfExists.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                 if(!snapshot.exists()){
                     userInfo = UserInfo(user.displayName!!, user.email!!, user.uid)
                     dbRef.child(user.uid).setValue(userInfo)
                 }
                else{
                     val map = (snapshot.value as HashMap<*,*>)[user.uid] as HashMap<*,*>
                     userInfo = UserInfo()
                     userInfo.name = map["name"].toString()
                     userInfo.email = map["email"].toString()
                     userInfo.avatar = map["avatar"].toString()
                     userInfo.uid = map["uid"].toString()
                     if(map["games"] != null)
                        userInfo.games = map["games"] as MutableList<Boolean>
                 }
                name.value = userInfo.name
            }

        })
    }
    public fun GameOver(result : Boolean){
        userInfo.games.add(result)
        dbRef.child("${userInfo.uid}/games").setValue(userInfo.games)
    }
    public fun getGames(): MutableList<Boolean> {
        return userInfo.games
    }
    public fun uploadImage(filePath : Uri?){
        if(filePath != null){
            val id = UUID.randomUUID().toString()
            val ref = storageRef.child("images/${userInfo.uid}/$id")
            ref.putFile(filePath!!).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
                Toast.makeText(getApplication(), "Image Uploaded", Toast.LENGTH_SHORT).show()
                userInfo.avatar = id
                dbRef.child("${userInfo.uid}/avatar").setValue(id)
            }).addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(getApplication(), "Image Uploading Failed " + e.message, Toast.LENGTH_SHORT).show()
            })
        }else{
            Toast.makeText(getApplication(), "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }

    public fun getImagePath() : String{
        if(userInfo.avatar.isEmpty())
            return ""
        else
        return userInfo.avatar
    }
    public fun getUID() = userInfo.uid

}