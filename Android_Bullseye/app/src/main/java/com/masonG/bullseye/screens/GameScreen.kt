package com.masonG.bullseye.screens

import android.util.Log
import com.masonG.bullseye.ui.theme.BullseyeTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.masonG.bullseye.components.GamDetail
import com.masonG.bullseye.components.ResultDialog
import com.masonG.bullseye.components.TargetSlider
import kotlin.math.abs
import kotlin.random.Random


@Composable
fun GameScreen() {
    fun newTargetValue() = Random.nextInt(1, 100)
    var alertIsVisable by rememberSaveable { mutableStateOf (false) }
    var sliderValue by rememberSaveable { mutableStateOf(0.5f) }
    var targetValue by rememberSaveable { mutableStateOf(newTargetValue()) }

    val sliderToInt = (sliderValue * 100).toInt()

    var totalScore by rememberSaveable { mutableStateOf(0) }
    var currentRound by rememberSaveable { mutableStateOf(1) }

    fun differenceAmount() = abs(targetValue - sliderToInt)

    fun pointsForCurrentRound() : Int {
        val maxScore = 100
        val difference = differenceAmount()
        var bonus = 0

        if (difference == 0) {
            bonus = 100
        } else if (difference == 1) {
            bonus = 50
        }
       return (maxScore - difference) + bonus
    }

    fun startNewGame() {
        totalScore = 0
        currentRound = 1
        sliderValue = 0.5f
        targetValue = newTargetValue()
    }

    fun alertTitle() : Int {
        val difference = differenceAmount()
        val title: Int = when {
            difference == 0 -> {
                R.string.alert_title1
            }
            difference < 5 -> {
                R.string.alert_title2
            }
            difference <= 10 -> {
                R.string.alert_title3
            }
            else -> {
                R.string.alert_title4
            }
        }

        return title
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
                value = sliderValue,
                valueChanged = { value ->
                    sliderValue = value
                }
            )
            Button(onClick = {
                alertIsVisable = true
                totalScore += pointsForCurrentRound()
                Log.i("ALERT VISIBLE? ", alertIsVisable.toString())
            }) {
                Text(text = stringResource(R.string.hit_me_button_text))

            }
            GamDetail(
                totalScore = totalScore,
                round = currentRound,
                onStartOver = {startNewGame()},
                modifier = Modifier.fillMaxWidth())
        }
        Spacer(modifier = Modifier.weight(.5f))

        if (alertIsVisable) {
            ResultDialog(
                dialogTitle = alertTitle(),
                hideDialog = { alertIsVisable = false
                },
                sliderValue = sliderToInt,
                points = pointsForCurrentRound(),
                onRoundIncrement = {
                    currentRound += 1
                    targetValue = newTargetValue()
                }
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