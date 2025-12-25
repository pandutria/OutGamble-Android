package com.example.outgamble_android.data.repository

import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.outgamble_android.data.model.Consultation
import com.example.outgamble_android.data.model.ConsultationMessage
import com.example.outgamble_android.data.model.Doctor
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class ConsultationRepository {
    private val consultationDb = FirebaseHelper.getDb().getReference("consultations")
    private val consultationMessageDb = FirebaseHelper.getDb().getReference("consultations_message")
    private val consultationHistoryDb = FirebaseHelper.getDb().getReference("consultation_last_message")

    fun get(doctorId: String, userId: String, callback: (ResultState<Consultation>) -> Unit) {
        callback(ResultState.Loading)
        consultationDb.orderByChild("doctorId").equalTo(doctorId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        val consultation = child.getValue(Consultation::class.java)
                        if (consultation?.userId == userId) {
                            callback(ResultState.Success(consultation))
                            return
                        }
                    }
                    callback(ResultState.Error("err"))
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(ResultState.Error(error.message))
                }
            })
    }


    fun create(doctorId: String, userId: String, callback: (ResultState<String>) -> Unit) {
        callback(ResultState.Loading)
        val id = consultationDb.push().key ?: UUID.randomUUID().toString()
        val consultation = Consultation(
            id = id,
            userId = userId,
            doctorId = doctorId
        )

        consultationDb.child(id).setValue(consultation)
            .addOnSuccessListener {
                callback(ResultState.Success("success"))
            }
            .addOnFailureListener {
                callback(ResultState.Error("err"))
            }
    }

    fun getMessage(consutationId: String, callback: (ResultState<List<ConsultationMessage>>) -> Unit) {
        callback(ResultState.Loading)
        consultationMessageDb.orderByChild("consultationId").equalTo(consutationId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ConsultationMessage>()

                for (snap in snapshot.children) {
                    val message = snap.getValue(ConsultationMessage::class.java)
                    if (message != null) list.add(message)
                }
                callback(ResultState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                callback(ResultState.Error(error.message))
            }
        })
    }

    fun sendMessage(consultationId: String, senderId: String, text: String, callback: (ResultState<String>) -> Unit) {
        callback(ResultState.Loading)
        val id = consultationMessageDb.child(consultationId).push().key ?: UUID.randomUUID().toString()
        val message = ConsultationMessage(
            id = id,
            consultationId = consultationId,
            senderId = senderId,
            message = text
        )

        consultationMessageDb.child(id).setValue(message)
            .addOnSuccessListener {
                callback(ResultState.Success("success"))
            }
            .addOnFailureListener {
                callback(ResultState.Error("err"))
            }
    }

    fun updateMessage(consultationId: String, senderId: String, text: String, callback: (ResultState<String>) -> Unit) {
        val lastMessage = mapOf(
            "message" to text,
            "senderId" to senderId,
            "createdAt" to System.currentTimeMillis()
        )

        consultationHistoryDb.child(consultationId)
            .setValue(lastMessage)
            .addOnCompleteListener {
                callback(ResultState.Success(""))
            }
    }
}