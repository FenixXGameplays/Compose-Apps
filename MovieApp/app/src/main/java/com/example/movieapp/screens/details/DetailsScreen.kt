package com.example.movieapp.screens.details

import android.annotation.SuppressLint
import android.widget.PopupWindow
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.movieapp.model.Movie
import com.example.movieapp.model.getMovies
import com.example.movieapp.navigation.MovieScreens
import com.example.movieapp.screens.home.MainContent
import com.example.movieapp.widgets.MovieRow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, movieId: String?) {
    val movie = getMovies().find { it.id == movieId }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(containerColor = Color.Cyan, Color.Red, Color.Red, Color.White, Color.Red),
                title = {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Arrow Back",
                            tint = Color.White,
                            modifier = Modifier.clickable {
                                navController.popBackStack()
                            }
                        )
                        Spacer(modifier = Modifier.width(120.dp))
                        if (movie != null) {
                            Text(text = movie.name)
                        }else{
                            Text(text = "Movie name not found")
                        }
                    }

                },
            )
        },
    ) {
        Surface(modifier = Modifier
            .padding(it)
            .verticalScroll(rememberScrollState())) {
            Column {
                DetailsMovie(movie)

            }
        }

    }
    

}

@Composable
private fun DetailsMovie(movie: Movie?) {
    val openImage = remember {
        mutableStateOf(false)
    }
    val imageLoad = remember {
        mutableStateOf("")
    }
    Surface(
        modifier = Modifier
            .padding(40.dp)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            if (movie != null) {
                Surface(shape = RectangleShape, modifier = Modifier.size(200.dp)) {
                    AsyncImage(model = movie.poster, contentDescription = "Movie Poster Detail", contentScale = ContentScale.FillBounds)
                }
            }
            ShowInfo(title = "Director: ", subtitle = movie?.director)
            Spacer(modifier = Modifier.height(8.dp))
            ShowInfo(title = "Year: ", subtitle = movie?.year)
            Spacer(modifier = Modifier.height(8.dp))
            AnnotedStringDetails(title = "Genre: ", subtitle = movie?.genre.toString())
            Spacer(modifier = Modifier.height(8.dp))
            AnnotedStringDetails(title = "Plot: ", subtitle = movie?.plot.toString())
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            AnnotedStringDetails(title = "Actors: ", subtitle = movie?.actors.toString())
            Spacer(modifier = Modifier.height(8.dp))
            ShowInfo(title = "Rating: ", subtitle = movie?.rating)
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Gallery",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow (content = {
                items(movie?.images!!.size) {
                    AsyncImage(model = movie.images[it],
                        contentDescription = "Gallery",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = if (movie.images.size - 1 == it) 0.dp else 12.dp)
                            .clickable {
                                openImage.value = !openImage.value
                                imageLoad.value = movie.images[it]
                            }
                    )
                }
            })
            if (openImage.value) {
                ImageBigger(openImage, imageLoad.value)
            }

        }
    }
}

@Composable
private fun ImageBigger(
    openImage: MutableState<Boolean>,
    imageLoad: String,

) {
    AlertDialog(
        onDismissRequest = { openImage.value = false },
        confirmButton = { /*TODO*/ },
        text = {

                Surface(

                    shape = RectangleShape
                ) {
                    Column {
                        AsyncImage(
                            model = imageLoad,
                            contentDescription = "null",
                        )
                    }
                }

        }
    )
}


@Composable
private fun ShowInfo(subtitle: String? = "", title: String? = "") {

    Row {
        Text(
            text = title!!,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )
        Text(text = subtitle!!, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun AnnotedStringDetails(title: String, subtitle: String) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)) {
                append(title)
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 25.sp
                )
            ) {
                append(subtitle)
            }

        }
    )
}