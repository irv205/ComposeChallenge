package com.irv205.gamechallenge

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.irv205.gamechallenge.ui.theme.GameChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameChallengeTheme {
                ContentView()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentView(){

    val vm : MainViewModel = viewModel()

    Box(modifier = Modifier) {
        Column(modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            HeaderView(modifier = Modifier
                    .fillMaxSize()
                    .weight(0.25f),
                painter = painterResource(id = R.drawable.ic_aptivist_logo)
            )

            Column(modifier = Modifier
                    .fillMaxSize()
                    .weight(0.75f)
            ) {
                TabMenu(vm = vm)
                BodyView(modifier = Modifier.fillMaxSize(),vm = vm)
            }
        }
    }
}

@Composable
fun HeaderView(modifier: Modifier, painter: Painter){

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentDescription = "title",
            modifier = Modifier
                .width(300.dp)
                .height(150.dp)
                .align(Alignment.Center)
        )
        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = stringResource(id = R.string.header_view_title),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary
        )
    }

}

@Composable
fun Tab(modifier: Modifier = Modifier, title: String, onClick : () -> Unit){

    Box(modifier = modifier
        .clickable {
            onClick.invoke()
        }
        .height(60.dp)) {
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.Center),
            color = Color.White,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun TabMenu(vm: MainViewModel){

    Row(modifier = Modifier.fillMaxWidth()) {
        Tab(
            title = stringResource(id = R.string.tabCoin),
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.primary)
        ) {
            vm.switchViews(MainViewState.ViewCoin)
        }
        Tab(
            title = stringResource(id = R.string.tabDice),
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.primary)
        ) {
            vm.switchViews(MainViewState.ViewDice)
        }
    }
}

@Composable
fun BodyView(modifier: Modifier = Modifier, vm: MainViewModel){

    val viewState = remember {
        vm.containerState
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when(viewState.value){
            MainViewState.ViewCoin -> {
                DiceFlip(modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center))
            }
            MainViewState.ViewDice -> {
                FlipCoin(modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center), 800)
            }
        }
    }
}

@Composable
fun DiceFlip(modifier: Modifier = Modifier) {
    //val mContext = LocalContext.current
    //val diceSound = MediaPlayer.create(mContext, R.raw.sn_dice)
    var value by remember { mutableStateOf( 1) }
    var animationValue by remember { mutableStateOf(false) }

    val imgResource =
        when(value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(imgResource),
            contentDescription = value.toString(),
            modifier = Modifier
                .clickable {
                    value = (1..6).random()
                    animationValue = !animationValue
                }
        )
    }
}

@Composable
fun FlipCoin(modifier: Modifier = Modifier, speed: Int) {

    var coinValue by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (coinValue) 2880f else 0f,
        animationSpec = tween(speed),
        finishedListener = {
            when ((1..2).random()) {
                1 -> coinValue = true
                2 -> coinValue = false
            }
        }
    )

    Column(modifier = modifier) {
        Box(
            Modifier.fillMaxSize(),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        rotationY = rotation
                        //rotationZ = rotation
                        cameraDistance = 8 * density
                    }
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = if (coinValue) painterResource(id = R.drawable.ic_coin_back) else painterResource(id = R.drawable.ic_coin_front),
                        contentDescription = coinValue.toString(),
                        modifier = Modifier
                            .width(250.dp)
                            .height(250.dp)
                            .clickable { coinValue = !coinValue }
                    )
                }

            }
        }
    }
}

