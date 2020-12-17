package com.example.third.info

import android.provider.ContactsContract
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInfo (
    var name: String = "",
    var email: String = "",
    var uid : String = "",
    var avatar : String = "",
    var useGravatar : Boolean = false,
    var games : MutableList<Boolean> = mutableListOf<Boolean>()
)
