package com.codan.cinder.ui.shows

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.codan.cinder.data.local.domain.show.Genre
import com.codan.cinder.data.local.domain.show.Show
import com.codan.cinder.ui.navigation.THEMOVIEDB_IMAGES_BASE_URL
import kotlin.math.min

@Composable
fun LazyShowList(
    items: LazyPagingItems<Show>,
    genres: List<Genre>,
    onShowClick: (Long) -> Unit
) {
    Log.d("DEBUG", items.loadState.toString())
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { show -> show.remoteId },
        ) { show ->
            show?.let {
                ShowItem(
                    show = show,
                    genres = genres,
                    onShowClick = onShowClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowItem(
    show: Show,
    genres: List<Genre>,
    onShowClick: (Long) -> Unit
) {
    val posterSize = min(LocalDensity.current.run { 128.dp.toPx() }.toInt() / 100 * 100, 500)
    val backdropSize = LocalDensity.current.run {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }.let { min(it.toInt() / 100 * 100, 500) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .clickable { onShowClick(show.remoteId) }
                .height(128.dp)
        ) {
            AsyncImage(
                model = THEMOVIEDB_IMAGES_BASE_URL + "w" + backdropSize + show.backdropPath,
                contentDescription = "Backdrop",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .alpha(0.25f)
                    .fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    model = THEMOVIEDB_IMAGES_BASE_URL + "w" + posterSize + show.posterPath,
                    contentDescription = "Poster",
                    modifier = Modifier.fillMaxHeight()
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = show.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = genres.filter { it.localId in show.genreIds!! }
                                .joinToString(", ") { it.name!! },
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(0.7f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.weight(0.3f)
                        ) {
                            Text(
                                text = show.voteAverage.toString(),
                            )
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "Star rating",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
