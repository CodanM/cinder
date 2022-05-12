package com.codan.cinder.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MovieFilter
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector?, var title: String) {
    object Login : NavigationItem(route = "login", null, "Login")
    object FollowedShows : NavigationItem(route = "followed-shows", Icons.Outlined.MovieFilter, "Followed Shows")
    object AddFollowedShow : NavigationItem(route = "add-followed-show", null, "Add Shows")
    object UpdateFollowedShow : NavigationItem(route = "update-followed-show", null, "Update Shows")
    object BrowseShows : NavigationItem(route = "browse-shows", Icons.Outlined.Search, "Browse")
    object ShowDetails : NavigationItem(route = "show-details", null, "Show details")
}
