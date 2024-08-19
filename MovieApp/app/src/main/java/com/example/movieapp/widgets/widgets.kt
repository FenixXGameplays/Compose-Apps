package com.example.movieapp.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.movieapp.model.Movie
import com.example.movieapp.model.getMovies

@Composable
fun MovieRow(movie: Movie = getMovies()[0], onItemClick: (Movie) -> Unit){
    var expanded = remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth(),
//        .clickable {
//            onItemClick(movie)
//        },
//        onClick = {
//          Another way to call click an element
//        },
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Surface(modifier = Modifier
                .padding(12.dp).
                size(150.dp),
                shape = RectangleShape,
                shadowElevation = 4.dp){
                AsyncImage(model = movie.poster, contentDescription = null, contentScale = ContentScale.FillBounds)
            }
            Column {
                Text(text = movie.name, fontWeight = FontWeight.ExtraBold)
                Text(text = movie.year)
                Row {

                    Icon(
                        imageVector = Icons.Default.Star,
                        tint = Color.Yellow,
                        contentDescription = null
                    )

                    Text(text = " ${movie.rating}")
                }
                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            expanded.value = !expanded.value
                        },
                    tint = Color.DarkGray,
                    imageVector = if(expanded.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Down Arrow"
                )
                AnimatedVisibility(visible = expanded.value) {
                    Column {
                        AnnotedString("Plot", subtitle = movie.plot)


                    }
                }
                Text(text = "More Info", modifier = Modifier.clickable {
                    onItemClick(movie)
                })

            }

        }

    }
}

@Composable
private fun AnnotedString(title: String, subtitle: String) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = 13.sp)) {
                append(title)
            }
            withStyle(
                style = SpanStyle(
                    color = Color.DarkGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light

                )
            ) {
                append(subtitle)
            }

        }, modifier = Modifier.padding(6.dp)
    )
}