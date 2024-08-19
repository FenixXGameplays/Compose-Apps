package com.example.movieapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieapp.MainActivity
import com.example.movieapp.model.Movie
import com.example.movieapp.screens.details.DetailScreen
import com.example.movieapp.screens.home.HomeScreen

@Composable
fun MovieNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MovieScreens.HomeScreen.name
    ){
        composable(route = MovieScreens.HomeScreen.name){
            HomeScreen(navController = navController)

        }

        composable(route = MovieScreens.DetailsScreen.name+"/{movieId}",
            arguments = listOf(
                navArgument(name = "movieId"){type = NavType.StringType},

            )){

            backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")!!
            DetailScreen(navController = navController, movieId = movieId)
        }
    }
}