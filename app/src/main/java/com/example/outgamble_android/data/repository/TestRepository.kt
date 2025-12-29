package com.example.outgamble_android.data.repository

import com.example.outgamble_android.data.model.Test
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TestRepository {
    private val db = FirebaseHelper.getDb().getReference("tests")

    fun get(callback: (ResultState<List<Test>>) -> Unit) {
        callback(ResultState.Loading)

        db.orderByChild("id").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list =  mutableListOf<Test>()

                for(snap in snapshot.children) {
                    val test = snap.getValue(Test::class.java)

                    if (test != null) list.add(test)
                }

                if (list.isEmpty()) return callback(ResultState.Error("err"))
                callback(ResultState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                callback(ResultState.Error(error.message))
            }
        })
    }
}