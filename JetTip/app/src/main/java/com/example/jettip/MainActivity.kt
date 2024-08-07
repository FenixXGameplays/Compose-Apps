package com.example.jettip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.jettip.components.InputField
import com.example.jettip.ui.theme.JetTipTheme
import com.example.jettip.utils.calculateTipTotal
import com.example.jettip.utils.calculateTotalPerPerson
import com.example.jettip.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp{
                Column {
                    TopHeader()
                    MainContent()
                }
            }
        }
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit){
    JetTipTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}





@Composable
private fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(15.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp)))
        , color = Color(0xFFE9D7F7)
            ) {
        Column(modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            val total = "%.2f".format(totalPerPerson)
            Text(text = "Total Per Person",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(text = "$$total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@Composable
private fun MainContent(){
    val totalSplitPerson = remember {
        mutableStateOf(1)
    }

    val totalBillState  = remember {
        mutableStateOf("0")
    }
    val validState  = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()

    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }
    val tipPercentage = (sliderPositionState.value * 100).toInt()

    val tipAmountState  = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState  = remember {
        mutableStateOf(0.0)
    }

    val range = IntRange(1,15)
    BillForm {}


}

@Composable
fun BillForm(modifier: Modifier = Modifier,

             onValChange: (String) -> Unit = {}
) {

    val totalSplitPerson = remember {
        mutableStateOf(1)
    }

    val totalBillState  = remember {
        mutableStateOf("0")
    }
    val validState  = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()

    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }
    val tipPercentage = (sliderPositionState.value * 100).toInt()

    val tipAmountState  = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState  = remember {
        mutableStateOf(0.0)
    }

    val range = IntRange(1,15)


    Column {
        TopHeader(
            totalPerPerson = totalPerPersonState.value.toDouble()
        )

        Surface(
            modifier = modifier
                .padding(2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {
            Column (modifier = Modifier.padding(all = 6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                InputField(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enable = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())
                        keyboardController?.hide()

                    }
                )

                if (validState) {
                    Row(
                        modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Split",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(120.dp))
                        Row(
                            modifier = Modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            RoundIconButton(imageVector = Icons.Default.Remove,
                                onClick = {
                                    if (totalSplitPerson.value > 1) {
                                        totalSplitPerson.value--
                                    }
                                    totalPerPersonState.value = calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = totalSplitPerson.value,
                                        tipPercentage = tipPercentage.toDouble()
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                text = "${totalSplitPerson.value}"
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            RoundIconButton(imageVector = Icons.Default.Add,
                                onClick = {
                                    if(totalSplitPerson.value < range.last) {
                                        totalSplitPerson.value++
                                    }
                                    totalPerPersonState.value = calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = totalSplitPerson.value,
                                        tipPercentage = tipPercentage.toDouble()
                                    )
                                }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 3.dp,
                            vertical = 12.dp
                        ),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Tip",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(200.dp))
//                        val total = "%.2f".format(tipAmountState.value)
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = "$${tipAmountState.value}"
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$tipPercentage%")
                        Spacer(modifier = Modifier.height(14.dp))
                        Slider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            value = sliderPositionState.value,
                            onValueChange = { newVal ->
                                sliderPositionState.value = newVal
                                tipAmountState.value = calculateTipTotal(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = newVal.toDouble()
                                )

                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = totalSplitPerson.value,
                                    tipPercentage = newVal.toDouble() * 100
                                )
                            },
                            steps = 5,
                            )
                    }


                }
            }
        }

    }
}

