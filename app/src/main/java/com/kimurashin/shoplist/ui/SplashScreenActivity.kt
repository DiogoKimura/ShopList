package com.kimurashin.shoplist.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kimurashin.shoplist.R
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}


@Composable
fun SplashScreen(onFinish: () -> Unit) {
    // Define o estado inicial da animação de escala
    var scale by remember { mutableStateOf(0.5f) }
    val scaleAnim by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(Unit) {
        // Atualiza o estado para acionar a animação
        scale = 1f
        // Atraso antes de sair da Splash
        delay(1500)
        onFinish()
    }

    // Exibe o conteúdo animado
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_background),
            contentDescription = "Logo",
            modifier = Modifier
                .scale(scaleAnim)
                .clip(CircleShape),
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen { }
}