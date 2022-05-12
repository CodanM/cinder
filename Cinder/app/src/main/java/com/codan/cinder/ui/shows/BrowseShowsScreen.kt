package com.codan.cinder.ui.shows

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.codan.cinder.components.FilterChip
import com.codan.cinder.data.local.domain.show.Show
import com.codan.cinder.data.remote.service.ServiceOperation
import com.codan.cinder.viewmodel.ShowViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BrowseShowsScreen(
    viewModel: ShowViewModel = hiltViewModel(),
    onShowClick: (Long) -> Unit
) {
    val genres by viewModel.genres.collectAsState(emptyList())

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val focusManager = LocalFocusManager.current

    var searchString by rememberSaveable { mutableStateOf("") }
    var searching by rememberSaveable { mutableStateOf(false) }
    var typeChipText by rememberSaveable { mutableStateOf("Movies") }
    var filterChipText by rememberSaveable { mutableStateOf("Filter") }
    var moviesChipEnabled by rememberSaveable { mutableStateOf(true) }

    val lazyDiscoverItems = if (!searching) when (typeChipText) {
        "Movies" -> viewModel.movies(ServiceOperation.Discover).collectAsLazyPagingItems()
        "TV Shows" -> viewModel.tvShows(ServiceOperation.Discover).collectAsLazyPagingItems()
        else -> throw Exception("Invalid type!")
    } else null
    val lazySearchItems = if (searching) when (typeChipText) {
        "Movies" -> viewModel.movies(ServiceOperation.Search, searchString).collectAsLazyPagingItems()
        "TV Shows" -> viewModel.tvShows(ServiceOperation.Search, searchString).collectAsLazyPagingItems()
        else -> throw Exception("Invalid type!")
    } else null

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                FilterChip(
                    enabled = moviesChipEnabled,
                    onClick = {
                        moviesChipEnabled = true
                        typeChipText = "Movies"
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    }
                ) {
                    Text(text = "Movies")
                }
                FilterChip(
                    enabled = !moviesChipEnabled,
                    onClick = {
                        moviesChipEnabled = false
                        typeChipText = "TV Shows"
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    }
                ) {
                    Text(text = "TV Shows")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 20.dp, end = 16.dp)
                .fillMaxSize()
        ) {
            SearchBar(
                value = searchString,
                onValueChange = { searchString = it },
                onSearch = {
                    searching = true
                    focusManager.clearFocus()
                },
                onClearValue = {
                    searchString = ""
                    searching = false
                    focusManager.clearFocus()
                },
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            {
                FilterChip(
                    enabled = typeChipText != "Type",
                    trailingIconVector = Icons.Default.ArrowDropDown,
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }
                    }
                ) {
                    Text(text = typeChipText)
                }
                FilterChip(
                    enabled = filterChipText != "Filter",
                    trailingIconVector = Icons.Default.FilterList,
                    onClick = {  }
                ) {
                    Text(text = filterChipText)
                }
            }
            LazyShowList(
                items = (lazyDiscoverItems ?: lazySearchItems!!) as LazyPagingItems<Show>,
                genres = genres,
                onShowClick = onShowClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearValue: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        trailingIcon = {
            Row {
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = onClearValue
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search text"
                        )
                    }
                }
                FilledIconButton(
                    shape = RoundedCornerShape(8.dp),
                    onClick = onSearch,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }
        },
        //colors = getMaterial3OutlinedTextFieldColors(),
        //shape = getMaterial3OutlinedTextFieldShape(),
        modifier = Modifier
            .fillMaxWidth()
    )
}

//@Preview
//@Composable
//fun SingleSeriesItemPreview() {
//    SingleSeriesItem(
//        show = Show(
//            "abc",
//            "Test",
//            "Test desc",
//            4.5f,
//            12,
//            "Netflix"
//        ),
//        onSeriesClick = {}
//    )
//}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(
        value = "Search text",
        onValueChange = {},
        onSearch = {},
        onClearValue = {},
    )
}