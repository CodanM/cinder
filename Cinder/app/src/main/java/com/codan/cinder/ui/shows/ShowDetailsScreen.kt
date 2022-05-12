package com.codan.cinder.ui.shows

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.codan.cinder.R
import com.codan.cinder.data.remote.service.ServiceOperation
import com.codan.cinder.viewmodel.FollowedShowViewModel
import com.codan.cinder.viewmodel.FollowedShowViewModelFactory
import com.codan.cinder.viewmodel.ShowViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDetailsScreen(
    showViewModel: ShowViewModel = hiltViewModel(),
    userId: String,
    showId: Long,
    onAdd: () -> Unit,
) {
    val context = LocalContext.current
    val followedShowViewModel: FollowedShowViewModel = viewModel(
        factory = FollowedShowViewModelFactory(context.applicationContext as Application)
    )
    val shows = showViewModel.movies(ServiceOperation.Discover).collectAsLazyPagingItems()
    val show = shows.itemSnapshotList.find { it?.remoteId == showId }

    show?.let {
        Card(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.image_placeholder),
                        contentDescription = "Series thumbnail",
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = show.name,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${show.voteAverage} / 10"
                            )
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "Star rating",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .height(300.dp)
                        .padding(bottom = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column {
                        Text(
                            text = "Description",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = show.overview
                        )
                    }

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Number of episodes"
                        )
//                        Text(
//                            text = show.numberOfEpisodes.toString(),
//                            modifier = Modifier.align(Alignment.CenterHorizontally)
//                        )
                    }
                    Column {
                        Text(
                            text = "Release Date"
                        )
                        Text(
                            text = ShowDetailsDateFormatter.format(show.releaseDate),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    TextButton(
                        onClick = {
//                            followedShowViewModel.addFollowedShow(FollowedShow(
//                                title = show.title,
//                                description = show.overview,
//                                rating = show.vote_average,
//                                episodeCount = show.episodeCount,
//                                platform = show.platform,
//                                userId = userId,
//                                showId = showId,
//                                currentEpisode = 1,
//                            ))
                            onAdd()
                        },
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Add to my series",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

private val ShowDetailsDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")