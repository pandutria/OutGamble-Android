package com.example.outgamble_android.data.repository

import com.example.outgamble_android.data.model.Education
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class EducationRepository {
    private val db = FirebaseHelper.getDb().getReference("educations")

    fun get(callback: (ResultState<List<Education>>)-> Unit) {
        callback(ResultState.Loading)
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Education>()

                for (snap in snapshot.children) {
                    val education = snap.getValue(Education::class.java)

                    if (education != null) list.add(education)
                }
                callback(ResultState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                callback(ResultState.Error(error.toString()))
            }
        })
    }
}