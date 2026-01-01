package com.example.outgamble_android.data.repository

import com.example.outgamble_android.data.model.User
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class AuthRepository {
    private val db = FirebaseHelper.getDb().getReference("users")

    fun register(
        fullname: String,
        username: String,
        password: String,
        callback: (ResultState<String>) -> Unit
    ){
        callback(ResultState.Loading)

        val id = db.push().key ?: UUID.randomUUID().toString()
        val user = User(
            id = id,
            username = username,
            fullname = fullname,
            password = password
        )

        db.child(id).setValue(user)
            .addOnSuccessListener {
                callback(ResultState.Success("Register berhasil"))
            }
            .addOnFailureListener {
                callback(ResultState.Error("Register gagal"))
            }
    }

    fun login(username: String, password: String, callback: (ResultState<User>) -> Unit) {
        db.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnap in snapshot.children) {
                    val user = userSnap.getValue(User::class.java)

                    if (user != null && user.username == username && user.password == password) {
                        callback(ResultState.Success(user))
                        return
                    }
                }
                callback(ResultState.Error("err"))
            }

            override fun onCancelled(error: DatabaseError) {
                callback(ResultState.Error("Terjadi kesalahan saat login"))
            }
        })
    }
}