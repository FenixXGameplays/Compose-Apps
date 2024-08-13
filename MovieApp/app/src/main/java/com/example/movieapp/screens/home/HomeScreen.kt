package com.example.movieapp.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movieapp.model.Movie
import com.example.movieapp.navigation.MovieScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(navController: NavController){
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarColors(containerColor = Color.Red, Color.Red, Color.Red, Color.White, Color.Red),
            title = {
                Text(text = "Movies")
            }
        )
    },
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column {
                MainContent(navController = navController)

            }
        }
    }
}

@Composable
fun MovieRow(movie: Movie, onItemClick: (Movie) -> Unit){
    Card(modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth()
        .clickable {
            onItemClick(movie)
        }
        .height(130.dp),
//        onClick = {
//          Another way to call click an element
//        },
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            Surface(modifier = Modifier
                .padding(12.dp)
                .size(100.dp),
                shape = RectangleShape,
                shadowElevation = 4.dp){
                Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Movie Image")
            }
            Text(text = movie.name)
        }
    }
}

@Composable
fun MainContent(
    navController: NavController,
    movieList: List<Movie> = listOf(
    Movie("Avatar", "James Cameron", "2009"),
        Movie("300", "Zack Snyder", "2007"),
        Movie("Harry Potter", "Chris Columbus", "2001"),
        Movie("Invasión a la tierra", "Jonathan Liebesman", "2011"),
        Movie("Inside Out", "Peter Docter", "2015"),
        Movie("Alien el octavo pasajero", "Ridley Scott", "1979"),
        Movie("Buscando a Dory", "Andrew Stanton", "2016"),
        Movie("Monster Inc", "Pete Docter", "2001"),
        Movie("Shrek", "Andrew Adamson", "2001"),
        Movie("Fast And Furious", "Rob Cohen", "2001"),
)){
    Surface (color = MaterialTheme.colorScheme.background) {
        LazyColumn (modifier = Modifier.padding(12.dp),content = {
            items(movieList.size) {
                MovieRow(movie = movieList[it]){ movie ->
                    navController.navigate(route = MovieScreens.DetailsScreen.name+"/${movie.name}/${movie.director}/${movie.year}")

                }

            }
        })
    }
}