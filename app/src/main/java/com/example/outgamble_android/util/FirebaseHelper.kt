package com.example.outgamble_android.util

import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {
    private fun getUrl(): String {
        val url = "https://outgamble-android-default-rtdb.asia-southeast1.firebasedatabase.app/"
        return url
    }
    fun getDb(): FirebaseDatabase {
        val db = FirebaseDatabase.getInstance(getUrl())
        return db
    }
}