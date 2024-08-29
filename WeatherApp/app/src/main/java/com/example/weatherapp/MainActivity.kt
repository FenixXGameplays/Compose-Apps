package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.graphics.drawable.toDrawable
import com.example.weatherapp.Service.WeatherService
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewModel.WeatherState
import com.example.weatherapp.viewModel.WeatherViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    private val weatherviewModel: WeatherViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                WeatherScreen(weatherViewModel = weatherviewModel)
            }
        }
        weatherviewModel.fetchWeatherData("Madrid")
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel){
    val weatherState by weatherViewModel.weatherState.collectAsState()
    Scaffold(modifier = Modifier.padding(top = 16.dp)) {
        HomeScreen(weatherViewModel = weatherViewModel)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(weatherViewModel: WeatherViewModel) {
    val weatherState by weatherViewModel.weatherState.collectAsState()


    Surface(modifier = Modifier.verticalScroll(rememberScrollState())) {
        EditTextButtonBar(weatherViewModel = weatherViewModel)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 90.dp, start = 15.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MainInfo(weatherState = weatherState)

        }
    }

}

@Composable
fun Weather_app(weatherData: WeatherModel) {
    //getDrawable(LocalContext.current, R.drawable.sol)
    val drawabletoShow = when(weatherData.weather[0].main){
        "Clouds" -> getDrawable(LocalContext.current, R.drawable.nubes)
        "Rain" -> getDrawable(LocalContext.current, R.drawable.lluvia)
        "Clear" -> getDrawable(LocalContext.current, R.drawable.sol)
        else -> {getDrawable(LocalContext.current, R.drawable.sol)}
    }
    Image(modifier = Modifier.size(250.dp).padding(top = 8.dp),
        painter = rememberDrawablePainter(
            drawable = drawabletoShow
        ),
        contentDescription = "Image",
        contentScale = ContentScale.FillWidth
    )

}

@Composable
fun MainInfo(weatherState: WeatherState) {
    Column(
        modifier = Modifier.padding(top = 20.dp, end = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isVisible by remember {
            mutableStateOf(false)
        }
        when (weatherState) {
            is WeatherState.Success -> {
                val weatherData = weatherState.weather
                Weather_app(weatherData = weatherData)
                MainData(weatherData)
                Spacer(modifier = Modifier.height(20.dp))

                Button(shape = RoundedCornerShape(5.dp),
                    onClick = {
                    isVisible=!isVisible

                }) {
                    Text(text = if(isVisible)"Less Details" else "Expand details")
                }
                Spacer(modifier = Modifier.height(20.dp))
                MoreData(isVisible, weatherData)
                Text(text = "Developed By Enrique Fernández ")

            }
            is WeatherState.Loading -> {
                CircularProgressIndicator()
            }
            is WeatherState.Error -> {
                Text(
                    text = "Failed to fetch weather data.",
                    fontSize = 18.sp,
                    color = Color.Red
                )
            }
        }
    }

}

@Composable
@OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class)
private fun ColumnScope.MoreData(
    isVisible: Boolean,
    weatherData: WeatherModel
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(initialScale = 2f) + expandVertically(),
        exit = scaleOut(targetScale = 2f) + shrinkVertically()

    ) {
        val sunrise = Instant.ofEpochSecond(weatherData.sys.sunrise.toLong())
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))
        val sunset = Instant.ofEpochSecond(weatherData.sys.sunset.toLong())
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))
        val itemsList = listOf(
            "Min Temp: ${(weatherData.main.temp_min- 273.15).toString().substring(0,5)}ºC",
            "Max Temp: ${(weatherData.main.temp_max- 273.15).toString().substring(0,5)}ºC",
            "Feels like: ${(weatherData.main.feels_like- 273.15).toString().substring(0,5)}ºC",
            "Humidity: ${weatherData.main.humidity}%",
            "Sunrise: $sunrise",
            "Sunset: $sunset",
            "Cloud ${weatherData.clouds.all}%",
            "Wind: ${weatherData.wind.speed}Km/h"
        )
        val columns = 2 // Number of columns in the grid
        val rows = itemsList.chunked(columns)
        Column {
            for (rowItems in rows) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    for (item in rowItems) {

                        Card(
                            modifier = Modifier
                                .width(180.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                Color.LightGray.copy(alpha = 0.5f)
                            ),
                            border = BorderStroke(2.dp, Color.Red),
                            elevation = CardDefaults.cardElevation(0.dp),
                        ) {

                            Text(
                                text = item,
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(20.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .animateEnterExit(enter = slideInVertically())
                            )

                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))


            }
        }
    }
}

@Composable
private fun MainData(weatherData: WeatherModel) {

    Text(
        text = "${(weatherData.main.temp - 273.15).toString().substring(0,5)}°C",
        fontSize = 45.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
    )
    Text(
        text = "${weatherData.name}, ${weatherData.sys.country}",
        fontSize = 30.sp,
        color = Color.Black,
        fontWeight = FontWeight.Medium,
    )
    Text(
        text = weatherData.weather[0].description,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        color = Color.DarkGray,
        modifier = Modifier.padding(top = 10.dp)
    )
}

@Composable
fun EditTextButtonBar(weatherViewModel: WeatherViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            value = text,
            onValueChange ={ newText ->
                text=newText
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 8.dp),
            label = { Text("Enter your city name")},
            shape = RoundedCornerShape(10.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription ="Search" )}
        )
        Button(onClick = {
            if(text.isNotBlank()){
                weatherViewModel.fetchWeatherData(city = text)
                text = ""
                keyboardController?.hide()
            }
        },
            modifier = Modifier.padding(start = 8.dp,end=10.dp),
            shape = RoundedCornerShape(10.dp)

        ) {
            Text(text = "Search")
        }
    }
}