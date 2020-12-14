package com.example.third.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.third.R
import com.example.third.UserGamesListAdapter
import com.example.third.UserViewModel
import com.example.third.utils.Utils
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.util.*


class UserFragment : Fragment() {
    private val dbUserRef = FirebaseDatabase.getInstance().getReference("users/${Firebase.auth.currentUser!!.uid}")
    private val storageRef = FirebaseStorage.getInstance().reference
    private lateinit var recyclerView : RecyclerView
    private lateinit var gamesAdapter : UserGamesListAdapter
    private lateinit var nameText : TextView
    private lateinit var imageView: ImageView
    private lateinit var gravatarButton: Button
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private val userViewModel : UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        gamesAdapter = UserGamesListAdapter(requireContext())
        gamesAdapter.setGames(userViewModel.getGames())
        recyclerView = view.findViewById(R.id.user_recyclerView)
        imageView = view.findViewById(R.id.user_imagePreview)
        gravatarButton = view.findViewById(R.id.user_gravatarButton)
        recyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = gamesAdapter
        }
        nameText = view.findViewById(R.id.user_nameTextView)
        userViewModel.name.observe(viewLifecycleOwner, Observer<String>{
            nameText.text = it
        })
        imageView.setOnClickListener {
            imagePick()
        }
        gravatarButton.setOnClickListener {
            val hash = Utils.md5(Firebase.auth.currentUser?.email!!)
            val gravatarUrl = "https://s.gravatar.com/avatar/$hash?s=80"
            Picasso.with(requireContext())
                .load(gravatarUrl)
                .into(imageView)
        }
        DownloadImage()
        return view
    }

    private fun imagePick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            try {
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                imageView.setImageBitmap(bitmap)
                userViewModel.uploadImage(filePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    public fun DownloadImage() {
        val path = userViewModel.getImagePath()
        if(path.isEmpty())
            return

        val localFile = File.createTempFile("image", ".jpeg")
        storageRef.child("images/${userViewModel.getUID()}/$path").getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.path)
            imageView.setImageBitmap(bitmap)
        }
    }

        companion object {
        @JvmStatic
        fun newInstance() =
            UserFragment()
    }
}