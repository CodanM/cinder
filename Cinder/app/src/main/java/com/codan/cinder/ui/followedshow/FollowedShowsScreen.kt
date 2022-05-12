package com.codan.cinder.ui.followedshow

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codan.cinder.R
import com.codan.cinder.data.local.domain.followedshow.FollowedShow
import com.codan.cinder.viewmodel.FollowedShowViewModel
import com.codan.cinder.viewmodel.FollowedShowViewModelFactory
import com.codan.cinder.ui.shows.SearchBar
import androidx.compose.foundation.lazy.items
import java.time.format.DateTimeFormatter

@Composable
fun FollowedShowsScreen(
    userId: String,
    onAddClick: () -> Unit,
    onUpdateClick: (followedShowID: String) -> Unit
) {
    val context = LocalContext.current
    val followedShowViewModel: FollowedShowViewModel = viewModel(
        factory = FollowedShowViewModelFactory(context.applicationContext as Application)
    )
    val listOfUserSeries = followedShowViewModel.followedShows.observeAsState(listOf()).value
    var filterString            by rememberSaveable { mutableStateOf("") }
    val errorDialogText         by remember { followedShowViewModel.errorText }

    val filteredListOfSeries = listOfUserSeries.filter { it.user!!.username == userId }.also {
        if (filterString.isNotBlank())
            it.filter { fs -> fs.show!!.name.contains(filterString) }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (filteredListOfSeries.isEmpty()) {
            CircularProgressIndicator()
        } else {
            SearchBar(
                value = filterString,
                onValueChange = { filterString = it },
                onSearch = {  },
                onClearValue = { filterString = "" }
            )
            Button(onClick = { onAddClick() }) {
                Text(text = "Add manually")
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(filteredListOfSeries) { item ->
                    SingleUserSeriesItem(
                        viewModel = followedShowViewModel,
                        followedShow = item,
                        onMinusClick = {
                            val updatedUser = item.copy(
                                currentEpisode = item.currentEpisode!! + (if (item.currentEpisode > 1) -1 else 0)
                            )
                            followedShowViewModel.updateFollowedShow(updatedUser)
                        },
                        onPlusClick = {
                            val updatedUser = item.copy(
                                currentEpisode = item.currentEpisode!! + 1
                            )
                            followedShowViewModel.updateFollowedShow(updatedUser)
                        },
                        onEditClick = {
                            onUpdateClick(item.id!!)
                        },
                        onDeleteClick = {
                            followedShowViewModel.deleteUserSeries(item.id!!)
                        }
                    )
                }
            }
        }
    }
    if (errorDialogText != null) {
        AlertDialog(
            onDismissRequest = { followedShowViewModel.errorText.value = null },
            text = { Text(text = errorDialogText ?: "") },
            confirmButton = {
                TextButton(onClick = { followedShowViewModel.errorText.value = null }) {
                    Text(text = "Ok")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleUserSeriesItem(
    viewModel: FollowedShowViewModel,
    followedShow: FollowedShow,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isUpdateDialogOpen by remember { mutableStateOf(false) }
    var isConfirmDeleteDialogOpen by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(0.9f)
                ) {
                    Text(
                        text = followedShow.show!!.name,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(0.7f)
                        ) {
                            Text(
                                text = "Episode: ",
                                fontSize = 16.sp
                            )
                            IconButton(onClick = { onMinusClick() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_remove_circle_outline_24),
                                    contentDescription = "Decrement episode button"
                                )
                            }
//                            Text(
//                                text = "${followedShow.currentEpisode} / ${followedShow.show.numberOfEpisodes}",
//                                fontSize = 16.sp
//                            )
                            IconButton(onClick = { onPlusClick() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                                    contentDescription = "Increment episode button"
                                )
                            }
                        }
                        Text(
                            text = FollowedShowsDateFormatter.format(followedShow.show.releaseDate)
                        )
                    }

                }
                Column(modifier = Modifier.weight(0.1f)) {
                    IconButton(
                        onClick = { isUpdateDialogOpen = true }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_edit_24),
                            contentDescription = "Edit button",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = { isConfirmDeleteDialogOpen = true }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_delete_24),
                            contentDescription = "Delete button",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
    if (isConfirmDeleteDialogOpen) {
        AlertDialog(
            onDismissRequest = { isConfirmDeleteDialogOpen = false },
            title = { Text(text = "Confirmation") },
            text = { Text(text = "Are you sure you want to delete the selected item?")},
            confirmButton = {
                TextButton(onClick = {
                    onDeleteClick()
                    isConfirmDeleteDialogOpen = false
                }) {
                    Text(text = "Confirm")
                }

            },
            dismissButton = {
                TextButton(onClick = { isConfirmDeleteDialogOpen = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

private val FollowedShowsDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")



