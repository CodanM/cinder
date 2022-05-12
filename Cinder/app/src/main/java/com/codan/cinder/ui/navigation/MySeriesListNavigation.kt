package com.codan.cinder.ui.navigation

import android.app.Application
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.codan.cinder.ui.shows.ShowDetailsScreen
import com.codan.cinder.ui.followedshow.AddFollowedShowScreen
import com.codan.cinder.ui.followedshow.FollowedShowsScreen
import com.codan.cinder.ui.followedshow.UpdateFollowedShowScreen
import com.codan.cinder.ui.shows.BrowseShowsScreen
import com.codan.cinder.ui.user.LoginScreen
import com.codan.cinder.viewmodel.UserViewModel
import com.codan.cinder.viewmodel.UserViewModelFactory

const val THEMOVIEDB_IMAGES_BASE_URL = "https://image.tmdb.org/t/p/"

@Composable
fun Navigation(navController: NavHostController) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as Application)
    )
    var currentUser by remember { userViewModel.currentUser }

    NavHost(navController, startDestination = NavigationItem.Login.route) {
        composable(NavigationItem.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    currentUser = it
                    navController.popBackStack()
                    navController.navigate(NavigationItem.FollowedShows.route)
                }
            )
        }
        composable(NavigationItem.FollowedShows.route) {
            FollowedShowsScreen(
                userId = currentUser!!.username,
                onAddClick = {
                    navController.navigate(NavigationItem.AddFollowedShow.route)
                },
                onUpdateClick = { followedShowId ->
                    navController.navigate("${NavigationItem.UpdateFollowedShow.route}/$followedShowId")
                }
            )
        }
        composable(NavigationItem.AddFollowedShow.route) {
            AddFollowedShowScreen()
        }
        composable(
            route = "${NavigationItem.UpdateFollowedShow.route}/{selectedFollowedShowId}",
            arguments = listOf(navArgument("selectedFollowedShowId") { type = NavType.StringType })
        ) {
            it.arguments?.getString("selectedFollowedShowId")?.let { id ->
                UpdateFollowedShowScreen(followedShowId = id)
            }
        }
        composable(NavigationItem.BrowseShows.route) {
            BrowseShowsScreen(
                onShowClick = { showIndex ->
                    navController.navigate("${NavigationItem.ShowDetails.route}/$showIndex")
                }
            )
        }
        composable(
            route = "${NavigationItem.ShowDetails.route}/{selectedShowId}",
            arguments = listOf(navArgument("selectedShowId") { type = NavType.LongType })
        ) {
            it.arguments?.getLong("selectedShowId")?.let { id ->
                ShowDetailsScreen(
                    userId = currentUser!!.username,
                    showId = id
                ) { navController.navigate(NavigationItem.FollowedShows.route) }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.FollowedShows,
        NavigationItem.BrowseShows
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute != NavigationItem.Login.route) {
        NavigationBar {
            items.forEach {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = it.icon!!,
                            contentDescription = it.title
                        )
                    },
                    label = { Text(text = it.title) },
                    alwaysShowLabel = true,
                    selected = currentRoute == it.route,
                    onClick = {
                        navController.navigate(it.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    //BottomNavigationBar()
}
