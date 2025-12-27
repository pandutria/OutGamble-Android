package com.example.outgamble_android.data.repository

import com.example.outgamble_android.data.model.Report
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.presentation.report.ReportsActivity
import com.example.outgamble_android.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class ReportRepository {
    private val db = FirebaseHelper.getDb().getReference("reports")

    fun createOfflineReport(imageUrl: String, location: String, date: String, desc: String, userId: String, callback: (ResultState<Report>) -> Unit) {
        callback(ResultState.Loading)

        val id = db.push().key ?: UUID.randomUUID().toString()

        val report = Report(
            id = id,
            image = imageUrl,
            location = location,
            date = date,
            description = desc,
            userId = userId
        )

        db.child(id).setValue(report)
            .addOnSuccessListener {
                callback(ResultState.Success(report))
            }
            .addOnFailureListener {
                callback(ResultState.Error(it.message!!))
            }
    }

    fun createOnlineReport(site: String, date: String, desc: String, platform: String, userId: String, callback: (ResultState<Report>) -> Unit) {
        callback(ResultState.Loading)

        val id = db.push().key ?: UUID.randomUUID().toString()

        val report = Report(
            id = id,
            site = site,
            date = date,
            platform = platform,
            description = desc,
            userId = userId
        )

        db.child(id).setValue(report)
            .addOnSuccessListener {
                callback(ResultState.Success(report))
            }
            .addOnFailureListener {
                callback(ResultState.Error(it.message!!))
            }
    }
}