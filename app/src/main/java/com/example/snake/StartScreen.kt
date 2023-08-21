package com.example.snake

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.snake.ui.theme.SnakeTheme
import kotlin.system.exitProcess

@Composable
fun StartScreen(
    navController: NavController,
){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)
    ){

        Column(
            modifier = Modifier
                .fillMaxWidth(0.75F)
                .fillMaxHeight(0.5F)
                .align(Alignment.Center)
        ) {
            Text(
                text = "SNAKE",
                fontSize = 52.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().weight(2f)
            )
            Button(
                onClick = { navController.navigate("game_screen") },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(10.dp)
            ) {
                Text(
                    text = "Start",
                    fontSize = 24.sp
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(10.dp)
            ) {
                Text(
                    text = "Options",
                    fontSize = 24.sp
                )
            }
            Button(
                onClick = { exitProcess(0) },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(10.dp)
            ) {
                Text(
                    text = "Exit",
                    fontSize = 24.sp
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartPreview() {
    SnakeTheme {
        StartScreen(navController = rememberNavController())
    }
}