package com.example.billtip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billtip.components.InputField
import com.example.billtip.ui.theme.BillTipTheme
import com.example.billtip.util.calculateTotalPerPerson
import com.example.billtip.util.calculateTotalTip
import com.example.billtip.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        MainContent()
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    BillTipTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()

        }
    }
}


@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
        .height(150.dp),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.inversePrimary) {
            Column(
                modifier = Modifier.padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val total = "%.2f".format(totalPerPerson)
                Text("Total Per Person",
                    style = MaterialTheme.typography.titleLarge)
                Text(text = "$$total",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold)
            }
    }

}

@Composable
fun MainContent() {

BillForm() {

}
}
@Preview
@Composable
fun BillForm(modifier: Modifier = Modifier,
             onValChange: (String) -> Unit = {}) {

    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()


    val splitByState = remember {
        mutableStateOf(1)
    }
    val range = IntRange(start = 1, endInclusive = 100)

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }


    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.inversePrimary)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TopHeader(totalPerPerson = totalPerPersonState.value)
            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
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
                        "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
//                        RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
//                            splitByState.value =
//                                if (splitByState.value > 1) splitByState.value - 1
//                                else 1
//                            totalPerPersonState.value = calculateTotalPerPerson(
//                                totalBill = totalBillState.value.toDouble(),
//                                splitBy = splitByState.value,
//                                tipPercentage = tipPercentage
//                            )
//
//                        })

                        IconButton(
                            onClick = {
                                splitByState.value =
                                if (splitByState.value > 1) splitByState.value - 1
                                else 1
                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitByState.value,
                                tipPercentage = tipPercentage
                            )
                            },
                            modifier = Modifier,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.inversePrimary
                            )
                        }


                        Text(
                            "${splitByState.value}", modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )

//                        RoundIconButton( imageVector = Icons.Default.Add, onClick = {
//                            if(splitByState.value < range.last) {
//                                splitByState.value = splitByState.value + 1
//                            }
//                            totalPerPersonState.value = calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
//                                splitBy = splitByState.value,
//                                tipPercentage = tipPercentage
//                            )
//                        })
                        IconButton(
                            onClick = {
                                if (splitByState.value < range.last) {
                                    splitByState.value = splitByState.value + 1
                                }
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = tipPercentage
                                )
                            },
                            modifier = Modifier,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    }


                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(
                        text = "$ ${tipAmountState.value}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )

                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$tipPercentage %",
                    )
                    Spacer(modifier = Modifier.height(14.dp))


                    Slider(value = sliderPositionState.value, onValueChange = { newVal ->
                        sliderPositionState.value = newVal
                        tipAmountState.value = calculateTotalTip(
                            totalBill = totalBillState.value.toDouble(),
                            tipPercentage
                        )
                        totalPerPersonState.value = calculateTotalPerPerson(
                            totalBill = totalBillState.value.toDouble(),
                            splitBy = splitByState.value,
                            tipPercentage = tipPercentage
                        )
                    }, modifier = Modifier.padding(start = 15.dp, end = 15.dp))
                }


            } else {
                Box() {}
            }
        }
    }
}

