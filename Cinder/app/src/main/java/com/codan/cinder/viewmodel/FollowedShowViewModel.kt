package com.codan.cinder.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.codan.cinder.data.local.domain.followedshow.FollowedShow
import com.codan.cinder.data.local.domain.validator.InvalidFollowedShowField
import com.codan.cinder.data.repository.FollowedShowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.*

class FollowedShowViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(FollowedShowViewModel::class.java))
            return FollowedShowViewModel(application) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class FollowedShowViewModel(application: Application): AndroidViewModel(application) {
    private val repository = FollowedShowRepository()
    private val _allUserSeries = MutableLiveData<List<FollowedShow>>()
    val followedShows: LiveData<List<FollowedShow>> = _allUserSeries

    val errorText = mutableStateOf(null as String?)

    @SuppressLint("MutableCollectionMutableState")
    val invalidFormFields = mutableStateOf(EnumSet.noneOf(InvalidFollowedShowField::class.java))

    init {
        fetchUserSeries()
    }

    private fun fetchUserSeries() {
        repository.fetchFollowedShows(_allUserSeries)
    }

    fun addFollowedShow(followedShow: FollowedShow) {
        viewModelScope.launch(Dispatchers.IO) {
            val addedId = repository.addFollowedShow(followedShow)
            if (addedId == null)
                errorText.value = (errorText.value ?: "") + "Could not complete add operation"
        }
    }

    fun updateFollowedShow(followedShow: FollowedShow) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFollowedShow(followedShow)
        }
    }

    fun deleteUserSeries(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFollowedShow(id)
        }
    }
}
