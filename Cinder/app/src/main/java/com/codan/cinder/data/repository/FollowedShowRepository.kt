package com.codan.cinder.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.codan.cinder.data.local.domain.followedshow.FollowedShow
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FollowedShowRepository {
    private val database = Firebase.database.reference
    private val followedShowsRef = database.child("followedShows")

    init {
        followedShowsRef.keepSynced(true)
        followedShowsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("SNAPSHOT", snapshot.value.toString())

                val allUserSeries: List<FollowedShow> = snapshot.children.map {
                    it.getValue(FollowedShow::class.java)!!
                }

                Log.i("ITEMS", allUserSeries.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("WARN", "FollowedShowRepository:init:onCancelled: ", error.toException())
            }
        })
    }

    fun fetchFollowedShows(liveData: MutableLiveData<List<FollowedShow>>) {
        followedShowsRef.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val followedShows: List<FollowedShow> = snapshot.children.map {
                        it.getValue(FollowedShow::class.java)!!
                    }

                    liveData.postValue(followedShows)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("WARN", "init:onCancelled: ", error.toException())
                }
            })
    }

    fun addFollowedShow(followedShow: FollowedShow): String? {
        Log.i("ADD", "addFollowedShow: $followedShow")
        val key = followedShowsRef.push().key
        if (key == null) {
            Log.w("WARN", "Couldn't get push key for UserSeries")
            return null
        }

        val userSeriesValues = followedShow.copy(id = key).toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/followedShows/$key"] = userSeriesValues
        database.updateChildren(childUpdates)
        return key
    }

    fun updateFollowedShow(followedShow: FollowedShow) {
        Log.i("UPDATE", "updateFollowedShow: $followedShow")
        followedShowsRef.child(followedShow.id!!).setValue(followedShow)
    }

    fun deleteFollowedShow(id: String) {
        Log.i("DELETE", "deleteFollowedShow: $id")
        followedShowsRef.child(id).removeValue()
    }
}
