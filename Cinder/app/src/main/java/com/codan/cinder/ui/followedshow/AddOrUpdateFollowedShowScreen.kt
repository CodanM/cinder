package com.codan.cinder.ui.followedshow

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codan.cinder.data.local.domain.validator.InvalidFollowedShowField
import com.codan.cinder.viewmodel.FollowedShowViewModel
import com.codan.cinder.viewmodel.FollowedShowViewModelFactory
import java.util.*

@Composable
fun AddFollowedShowScreen() {
    val context = LocalContext.current
    val viewModel: FollowedShowViewModel = viewModel(
        factory = FollowedShowViewModelFactory(context.applicationContext as Application)
    )

    var nameStr           by rememberSaveable { mutableStateOf("") }
    var descriptionStr    by rememberSaveable { mutableStateOf("") }
    var ratingStr         by rememberSaveable { mutableStateOf("5.0") }
    var episodeCountStr   by rememberSaveable { mutableStateOf("") }
    var platformStr       by rememberSaveable { mutableStateOf("") }
    var currentEpisodeStr by rememberSaveable { mutableStateOf("1") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = nameStr,
            label = { Text("Name") },
            onValueChange = {
                nameStr = it
            },
            isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.Name),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = descriptionStr,
            label = { Text("Description") },
            onValueChange = {
                descriptionStr = it
            },
            isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.Description),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ratingStr,
            label = { Text("Rating") },
            onValueChange = {
                ratingStr = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.Rating),
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = episodeCountStr,
                label = { Text("Episode count") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    episodeCountStr = it
                },
                isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.EpisodeCount),
                modifier = Modifier.weight(0.5f)
            )
            OutlinedTextField(
                value = currentEpisodeStr,
                label = { Text("Current episode") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    currentEpisodeStr = it
                },
                isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.CurrentEpisode),
                modifier = Modifier.weight(0.5f)
            )
        }
        OutlinedTextField(
            value = platformStr,
            label = { Text("Platform") },
            onValueChange = {
                platformStr = it
            },
            isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.Platform),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (validateForm(
                        viewModel,
                        nameStr,
                        descriptionStr,
                        ratingStr.toFloatOrNull(),
                        episodeCountStr.toIntOrNull(),
                        platformStr,
                        currentEpisodeStr.toIntOrNull()
                    )) {
                        viewModel.invalidFormFields.value = EnumSet.noneOf(InvalidFollowedShowField::class.java)
//                        viewModel.addFollowedShow(FollowedShow(
//                            show = Show(
//                                title = nameStr,
//                                overview = descriptionStr,
//                                voteAverage = ratingStr.toFloat(),
//
//                            ),
//                            currentEpisode = currentEpisodeStr.toInt()
//                        ))
                    }
                }
            ) {
                Text(text = "Add")
            }
        }
    }
}

fun validateForm(
    viewModel: FollowedShowViewModel,
    name: String?,
    description: String?,
    rating: Float?,
    episodeCount: Int?,
    platform: String?,
    currentEpisode: Int?,
): Boolean {
    val invalidFields = EnumSet.noneOf(InvalidFollowedShowField::class.java)
    invalidFields.apply {
        if (name.isNullOrBlank())
            add(InvalidFollowedShowField.Name)
        if (description.isNullOrBlank())
            add(InvalidFollowedShowField.Description)
        if (rating == null || rating < 1 || rating > 5)
            add(InvalidFollowedShowField.Rating)
        if (episodeCount == null || episodeCount < 1)
            add(InvalidFollowedShowField.EpisodeCount)
        if (platform.isNullOrBlank())
            add(InvalidFollowedShowField.Platform)
        if (currentEpisode == null || currentEpisode < 1 || ((episodeCount != null) && currentEpisode > episodeCount))
            add(InvalidFollowedShowField.CurrentEpisode)
    }
    viewModel.invalidFormFields.value = invalidFields
    if (invalidFields.isEmpty())
        return true
    return false
}

@Composable
fun UpdateFollowedShowScreen(
    followedShowId: String
) {
    val context = LocalContext.current
    val viewModel: FollowedShowViewModel = viewModel(
        factory = FollowedShowViewModelFactory(context.applicationContext as Application)
    )
    val followedShow = viewModel.followedShows.value?.find { it.id == followedShowId }!!
    val show = followedShow.show
    var nameStr           by rememberSaveable { mutableStateOf(show?.name ?: "") }
    var descriptionStr    by rememberSaveable { mutableStateOf(show?.overview ?: "") }
    var ratingStr         by rememberSaveable { mutableStateOf(show?.voteAverage.toString()) }
    var episodeCountStr   by rememberSaveable { mutableStateOf("") }
    var platformStr       by rememberSaveable { mutableStateOf(/*show.platform ?:*/ "") }
    var currentEpisodeStr by rememberSaveable { mutableStateOf(followedShow.currentEpisode.toString()) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = nameStr,
            label = { Text("Name") },
            onValueChange = {
                nameStr = it
            },
            isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.Name),
        )
        OutlinedTextField(
            value = descriptionStr,
            label = { Text("Description") },
            onValueChange = {
                descriptionStr = it
            },
            isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.Description),
        )
        OutlinedTextField(
            value = ratingStr,
            label = { Text("Rating") },
            onValueChange = {
                ratingStr = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.Rating),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = episodeCountStr,
                label = { Text("Episode count") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    episodeCountStr = it
                },
                isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.EpisodeCount),
                modifier = Modifier.weight(0.5f)
            )
            OutlinedTextField(
                value = currentEpisodeStr,
                label = { Text("Current episode") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    currentEpisodeStr = it
                },
                isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.CurrentEpisode),
                modifier = Modifier.weight(0.5f)
            )
        }
//        OutlinedTextField(
//            value = platformStr,
//            label = { Text("Platform") },
//            onValueChange = {
//                platformStr = it
//            },
//            isError = viewModel.invalidFormFields.value.contains(InvalidFollowedShowField.Platform)
//        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (validateForm(
                        viewModel,
                        nameStr,
                        descriptionStr,
                        ratingStr.toFloatOrNull(),
                        episodeCountStr.toIntOrNull(),
                        platformStr,
                        currentEpisodeStr.toIntOrNull()
                    )) {
                        viewModel.invalidFormFields.value = EnumSet.noneOf(InvalidFollowedShowField::class.java)
//                        viewModel.updateFollowedShow(FollowedShow(
//                            name = nameStr,
//                            description = descriptionStr,
//                            rating = ratingStr.toFloat(),
//                            episodeCount = episodeCountStr.toInt(),
//                            platform = platformStr,
//                            currentEpisode = currentEpisodeStr.toInt()
//                        ))
                    }
                }
            ) {
                Text(text = "Update")
            }
        }
    }
}