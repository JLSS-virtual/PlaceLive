package com.jlss.placelive.ui.Components
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.jlss.placelive.R

@Composable
fun MainAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.globair))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE // Loop animation infinitely
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(180.dp) // Adjust size to fit better
    )
}


@Composable
fun Loader(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loaderflower))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE // Loop animation infinitely
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(200.dp) // Adjust size to fit better
    )
}

@Composable
fun FriendLoader(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.friendloader))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE // Loop animation infinitely
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(200.dp) // Adjust size to fit better
    )
}

