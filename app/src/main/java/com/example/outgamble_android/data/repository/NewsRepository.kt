package com.example.outgamble_android.data.repository

import com.example.outgamble_android.data.model.News
import com.example.outgamble_android.data.state.ResultState
import com.example.outgamble_android.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class NewsRepository {
    private val db = FirebaseHelper.getDb().getReference("news")

    fun get(callback: (ResultState<List<News>>) -> Unit) {
        callback(ResultState.Loading)
        db.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newsList = mutableListOf<News>()

                for (newsSnap in snapshot.children) {
                    val news = newsSnap.getValue(News::class.java)
                    if (news != null) newsList.add(news)
                }

                if (newsList.isEmpty()) return callback(ResultState.Error("err"))

                callback(ResultState.Success(newsList))
            }

            override fun onCancelled(error: DatabaseError) {
                callback(ResultState.Error(error.message))
            }
        })
    }
}