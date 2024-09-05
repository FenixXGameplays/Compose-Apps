package com.example.weatherapp

import android.annotation.SuppressLint
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getDrawable
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.model.HourDailyModel
import com.example.weatherapp.model.HourInfo
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewModel.WeatherHourInfoState
import com.example.weatherapp.viewModel.WeatherNextDaysState
import com.example.weatherapp.viewModel.WeatherState
import com.example.weatherapp.viewModel.WeatherViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

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
        weatherviewModel.fetchDailyHourWeatherData("Madrid")
        //weatherviewModel.fetchWeatherNextDays("Madrid")
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
    val weatherHourInfoState by weatherViewModel.weatherHourInfoState.collectAsState()
    val weatherNextDaysState by weatherViewModel.weatherNextDaysState.collectAsState()


    Surface(modifier = Modifier.verticalScroll(rememberScrollState())) {
        EditTextButtonBar(weatherViewModel = weatherViewModel)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 90.dp, start = 15.dp, end = 10.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MainInfo(weatherState = weatherState,
                weatherNextDaysState = weatherNextDaysState,
                    weatherHourInfoState = weatherHourInfoState)

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
    Image(modifier = Modifier
        .size(250.dp)
        .padding(top = 8.dp),
        painter = rememberDrawablePainter(
            drawable = drawabletoShow
        ),
        contentDescription = "Image",
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun ShowIcon(typeData: String) {
    val drawabletoShow = when(typeData){
        "Clouds" -> getDrawable(LocalContext.current, R.drawable.nubes)
        "Rain" -> getDrawable(LocalContext.current, R.drawable.lluvia)
        "Clear" -> getDrawable(LocalContext.current, R.drawable.sol)
        else -> {getDrawable(LocalContext.current, R.drawable.sol)}
    }
    Image(
        painter = rememberDrawablePainter(
            drawable = drawabletoShow
        ),
        contentDescription = "Image",
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun MainInfo(weatherState: WeatherState,
             weatherNextDaysState: WeatherNextDaysState,
             weatherHourInfoState: WeatherHourInfoState) {
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


                ShowNextHours(weatherHourInfoState)

//                when(weatherNextDaysState){
//                    is WeatherNextDaysState.Success -> {
//                        val weatherNextDaysData = weatherNextDaysState.weather
//                        Image(painter = rememberAsyncImagePainter(model = weatherNextDaysData.iconUrl), contentDescription = null)
//                    }
//                    is WeatherNextDaysState.Error -> {
//                        Text(text = "Failed to get the next Days")
//                    }
//                    is WeatherNextDaysState.Loading -> {
//                        CircularProgressIndicator()
//                    }
//                }

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
private fun ShowNextHours(weatherHourInfoState: WeatherHourInfoState) {
    when (weatherHourInfoState) {
        is WeatherHourInfoState.Success -> {
            val weatherHourInfoState = weatherHourInfoState.weather
            val today = LocalDate.now()
            val tomorrow = today.plusDays(1)
            ShowHourTemp(weatherHourInfoState, today, tomorrow)
        }

        is WeatherHourInfoState.Error -> {
            Text(text = "Failed to get the Hour info")
        }

        is WeatherHourInfoState.Loading -> {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ShowHourTemp(
    weatherHourInfoState: HourDailyModel,
    today: LocalDate,
    tomorrow: LocalDate,
) {
    Column {
        Text(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            text = "Próximas horas",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
        )
        LazyRow(verticalAlignment = Alignment.CenterVertically,
            content = {
                items(weatherHourInfoState.list.size) { index ->
                    val data = weatherHourInfoState.list[index]
                    val dateFormatted = data.dt_txt.substring(0, data.dt_txt.indexOf(" "))
                    if (dateFormatted == today.toString() || dateFormatted == tomorrow.toString()) {
                        Card(
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(),
                            modifier = Modifier
                                .width(150.dp)
                                .padding(6.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ShowTodayOrTomorrowText(dateFormatted, tomorrow, today)
                                getHour(data)
                                ShowIcon(typeData = data.weather[0].main)
                                val temp = (data.main.temp - 273.15).toString().substring(0, 2)
                                ShowProbabilityOfRain(data, temp)
                                ShowTempNextHours(data, temp)
                            }

                        }
                    }
                }
            })
    }
    
}

@Composable
private fun ShowTempNextHours(data: HourInfo, temp: String) {
    Text(
        modifier = Modifier.padding(8.dp),
        text = "${
            (data.main.temp - 273.15)
                .toString().substring(0, 2)
        }ºC",
        style = getColor(temp)
    )
}

@Composable
private fun ShowProbabilityOfRain(data: HourInfo, temp: String) {
    if (data.pop * 100 > 0 && data.pop * 100 < 10) {
        Text(
            text = "Probability of Rain: ${
                (data.pop * 100)
                    .toString()
                    .substring(
                        0,
                        1
                    )
            }%",
            style = getColor(temp)
        )
    } else if (data.pop * 100 >= 10) {
        Text(
            text = "Probability of Rain: ${
                (data.pop * 100)
                    .toString()
                    .substring(
                        0,
                        data.pop.toString().indexOf('.') + 1
                    )
            }%",
            style = getColor(temp)
        )
    } else {
        Text(text = "")
    }
}

@Composable
private fun getHour(data: HourInfo) {
    Text(
        text = data.dt_txt.substring(
            data.dt_txt.indexOf(" ") + 1,
            data.dt_txt.length - 3
        ) +
                getPmAm(
                    data.dt_txt.substring(
                        data.dt_txt.indexOf(" ") + 1,
                        data.dt_txt.length - 3
                    )
                ),
        fontSize = 15.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun ShowTodayOrTomorrowText(
    dateFormatted: String,
    tomorrow: LocalDate,
    today: LocalDate
) {
    if (dateFormatted == tomorrow.toString()) {
        Text(
            text = "Tomorrow",
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
        )
    } else if (dateFormatted == today.toString()) {
        Text(
            text = "Today",
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
        )
    }
}

fun getPmAm(text: String): String {
    if(text.substring(0,2).toInt() >= 12){
        return "pm"
    }
    return "am"
}

fun getColor(temp: String): TextStyle {
    val color = if(temp.toInt() > 20){
        Color.Red
    }else{
        Color.Blue
    }
    return TextStyle(color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
            "Min Temp: ${(weatherData.main.temp_min- 273.15).toString().substring(0,2)}ºC",
            "Max Temp: ${(weatherData.main.temp_max- 273.15).toString().substring(0,2)}ºC",
            "Feels like: ${(weatherData.main.feels_like- 273.15).toString().substring(0,2)}ºC",
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
        text = "${(weatherData.main.temp - 273.15).toString().substring(0,2)}°C",
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
                weatherViewModel.fetchDailyHourWeatherData(city = text)
                //weatherViewModel.fetchWeatherNextDays(city = text)
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