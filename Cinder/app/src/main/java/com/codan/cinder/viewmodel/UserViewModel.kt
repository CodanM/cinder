package com.codan.cinder.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.codan.cinder.data.local.domain.user.User
import com.codan.cinder.data.repository.UserRepository
import java.lang.IllegalArgumentException

class UserViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(UserViewModel::class.java))
            return UserViewModel(application) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _currentUser = mutableStateOf(null as User?)
    val currentUser = _currentUser

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        repository.fetchAllUsers(_users)
    }

}
