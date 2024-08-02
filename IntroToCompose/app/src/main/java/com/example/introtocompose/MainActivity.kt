package com.example.introtocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.introtocompose.ui.theme.IntroToComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntroToComposeTheme {
                MyApp()
            }
        }
    }
}

@Preview
@Composable
fun MyApp(){
    var clickedTimer = remember {
        mutableStateOf(0)
    }
    var showText = remember {
        mutableStateOf(false)
    }

    Surface(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(),
        color = Color.Cyan) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Times Clicked", modifier = Modifier.padding(bottom = 50.dp), color = Color.Magenta)
            CreateCircle(clickedTimer.value){//Teniendo en cuenta los parametros de la función es una manera de modificar el remember
                clickedTimer.value++
            }
            Button(onClick = { showText.value = !showText.value }) {
                Text(text = "Show Text How Many times Clicked")
            }
            if(showText.value) {
                Text(text = "${clickedTimer.value} times clicked", modifier = Modifier.padding(top = 50.dp), color = Color.Magenta)
            }
        }
    }
}

//@Preview
@Composable
fun CreateCircle(clickedTimer: Int = 0, updateClickerCounter: (Int) -> Unit) {
    //Con esto solamente tengo
    // que mandar el primer parametro dentro de la funcion y funciona como un callbackFunction

    Card(
        modifier = Modifier
            .padding(3.dp)
            .size(145.dp)
            .clickable {
                updateClickerCounter(clickedTimer)//
            },
        shape = CircleShape,
    ) {
       Box(contentAlignment = Alignment.Center){
           Text(text = "$clickedTimer", modifier = Modifier
               .fillMaxSize()
               .wrapContentSize(Alignment.Center))
       }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowAge(age: Int = 12){
    Text(text = "$age")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

