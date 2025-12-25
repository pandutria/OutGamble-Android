package com.example.outgamble_android.data.repository

import com.example.outgamble_android.data.model.Doctor
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DoctorRepository {
    private val db = FirebaseHelper.getDb().getReference("doctors")

    fun get(callback: (ResultState<List<Doctor>>) -> Unit) {
        callback(ResultState.Loading)
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Doctor>()

                for (doctorSnap in snapshot.children) {
                    val doctor = doctorSnap.getValue(Doctor::class.java)

                    if (doctor != null) list.add(doctor)
                }

                if(list.isEmpty()) return callback(ResultState.Error("err"))
                callback(ResultState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                callback(ResultState.Error(error.message))
            }
        })
    }

    fun getById(id: String, callback: (ResultState<Doctor>) -> Unit) {
        callback(ResultState.Loading)
        db.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val doctor = snapshot.getValue(Doctor::class.java)

                if (doctor != null) callback(ResultState.Success(doctor))
                else callback(ResultState.Error("err"))
            }

            override fun onCancelled(error: DatabaseError) {
                callback(ResultState.Error(error.message))
            }
        })
    }
}