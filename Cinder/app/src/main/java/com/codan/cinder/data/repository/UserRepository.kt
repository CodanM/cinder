package com.codan.cinder.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.codan.cinder.data.local.domain.user.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserRepository {
    private val usersRef = Firebase.database.getReference("users");

    init {
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("SNAPSHOT", snapshot.value.toString())

                val users: List<User> = snapshot.children.map {
                    it.getValue(User::class.java)!!
                }

                Log.i("ITEMS", users.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("WARN", "init:onCancelled: ", error.toException())
            }

        })
    }

    fun fetchAllUsers(liveData: MutableLiveData<List<User>>) {
        usersRef.orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users: List<User> = snapshot.children.map {
                        it.getValue(User::class.java)!!
                    }

                    liveData.postValue(users)
                    Log.i("SNAPSHOT", "onDataChange: $users")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("WARN", "init:onCancelled: ", error.toException())
                }
            })
    }
}
