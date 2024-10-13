package com.masonG.bullseye.screens

import android.util.Log
import com.masonG.bullseye.ui.theme.BullseyeTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masonG.bullseye.components.GamePrompt
import com.masonG.bullseye.R
import com.masonG.bullseye.components.ResultDialog
import com.masonG.bullseye.components.TargetSlider
import kotlin.math.abs
import kotlin.random.Random


@Composable
fun GameScreen() {
    var alertIsVisable by rememberSaveable { mutableStateOf (false) }
    var slidervalue by rememberSaveable { mutableStateOf(0.5f) }
    var targetValue by rememberSaveable { mutableStateOf(Random.nextInt(1,100)) }

    val sliderToInt = (slidervalue * 100).toInt()

    fun pointsForCurrentRound() : Int {
        val maxScore = 100
        val difference = abs(targetValue - sliderToInt)
       return maxScore - difference
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.weight(.5f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.weight(9f)
        ) {
            GamePrompt(targetValue = targetValue)
            TargetSlider(
                value = slidervalue,
                valueChanged = { value ->
                    slidervalue = value
                }
            )
            Button(onClick = {
                alertIsVisable = true
                Log.i("Alert visable? ", alertIsVisable.toString())
            }) {
                Text(text = stringResource(R.string.hit_me_button_text))

            }
        }
        Spacer(modifier = Modifier.weight(.5f))

        if (alertIsVisable) {
            ResultDialog(
                hideDialog = { alertIsVisable = false
                },
                sliderValue = sliderToInt,
                points = pointsForCurrentRound()
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 864, heightDp = 432)
@Composable
fun GameScreenPreview() {
    BullseyeTheme {
        GameScreen()
    }
}