package com.example.snake

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.snake.ui.theme.SnakeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    navController: NavController,
){
    var foodSegment by remember { mutableStateOf(Pair(4, 4)) }
    var snakeSegments by remember { mutableStateOf(listOf(Pair(0, 0))) }
    var direction by remember { mutableStateOf(Direction.RIGHT) }
    var runGame by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8F)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center, // Aligns content vertically at the center
            horizontalAlignment = Alignment.CenterHorizontally // Aligns content horizontally at the center
        ) {
            val gameScope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                gameScope.launch {
                    while (runGame) {
                        delay(200)
                        runGame = moveSnake(
                            direction = direction,
                            State(food = foodSegment, snake = snakeSegments),
                            onSnakeChanged = {
                                    newSnake -> snakeSegments = newSnake
                            },
                            onFoodChanged = {
                                    newFood -> foodSegment = newFood
                            }
                        )
                    }
                }
            }
            GameBoard(State(food = foodSegment, snake = snakeSegments), runGame = runGame,
                onChange = {
                    changedRunGame -> runGame = changedRunGame
                },
                onSnakeChanged = {
                        newSnake -> snakeSegments = newSnake
                },
                onFoodChanged = {
                        newFood -> foodSegment = newFood
                },
                navController = navController
            )
            Spacer(modifier = Modifier.weight(0.1F))
            ArrowButtonGrid(
                currentDirection = direction,
                onDirectionChanged = {
                        newDirection -> direction = newDirection // Update the state in the parent
                }
            )
        }
    }

}

fun moveSnake(
    direction: Direction,
    state: State,
    onSnakeChanged: (List<Pair<Int, Int>>) -> Unit,
    onFoodChanged: (Pair<Int, Int>) -> Unit
): Boolean {
    // Update the snake's position based on the current direction
    // Add logic to move the snake segments
    val moveDirection = when (direction) {
        Direction.LEFT -> Pair(-1, 0)
        Direction.RIGHT -> Pair(1, 0)
        Direction.UP -> Pair(0, -1)
        Direction.DOWN -> Pair(0, 1)
    }

    // Calculate the new head position after movement
    val newHead = Pair(
        (state.snake.first().first + moveDirection.first + 7) % 7,
        (state.snake.first().second + moveDirection.second + 7) % 7
    )

    val updatedSnakeSegments = if (newHead == state.food) {
        // Snake eats the food, so add the new head and keep the existing body
        listOf(newHead) + state.snake
    } else {
        // Snake moves, so update the body positions
        val newBody = state.snake.dropLast(1) // Remove the last segment
        listOf(newHead) + newBody
    }

    // Update the state with the new snake segments and potentially new food position
    if ( newHead in state.snake ) {
        return false
    } else{
        onSnakeChanged(updatedSnakeSegments)
    }

    if (newHead == state.food) {
        onFoodChanged(generateNewFoodPosition(state.snake)) // Generate new food position
    }
    return true
}

// Function to generate a new random food position
fun generateNewFoodPosition(snakeSegments: List<Pair<Int, Int>>): Pair<Int, Int> {
    var newFoodPosition: Pair<Int, Int> = Pair((0..6).random(), (0..6).random())
    while (newFoodPosition in snakeSegments) {
        newFoodPosition = Pair((0..6).random(), (0..6).random())
    }
    return newFoodPosition
}

data class State(val food: Pair<Int, Int>, val snake: List<Pair<Int, Int>>)

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

@Composable
fun GameBoard(state: State, runGame: Boolean, onChange: (Boolean) -> Unit, onSnakeChanged: (List<Pair<Int, Int>>) -> Unit, onFoodChanged: (Pair<Int, Int>) -> Unit, navController:NavController){
    val tileSize = 45.dp
    Box (
        Modifier
            .size(315.dp)
            .border(2.dp, Color.DarkGray)
    ){
        Box(
            Modifier
                .offset(x = tileSize * state.food.first, y = tileSize * state.food.second)
                .size(tileSize)
                .padding(4.dp)
                .background(
                    Color.Green, CircleShape
                )
        )
        state.snake.forEach {
            Box(
                modifier = Modifier
                    .offset(x = tileSize * it.first, y = tileSize * it.second)
                    .size(tileSize)
                    .border(2.dp, Color.LightGray)
                    .background(
                        Color.Black, RectangleShape
                    )
            )
        }

        if (!runGame){
            Box (
                Modifier
                    .size(315.dp)
                    .background(Color.DarkGray.copy(alpha = 0.5f)) // Adjust the alpha value for desired transparency
            ){
                Button(onClick = {
                    onChange(true)
                    onSnakeChanged(listOf(Pair(0, 0)))
                    onFoodChanged(Pair(4,4))
                    navController.navigate("game_screen")
                }, modifier = Modifier.align(Alignment.Center), colors = ButtonDefaults.buttonColors(containerColor = Color.Green)) {
                    Text(text = "Restart")
                }
            }
        }
    }
}

@Composable
fun ArrowButtonGrid(currentDirection: Direction, onDirectionChanged: (Direction) -> Unit) {
    val buttonModifiers = Modifier.size(64.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { if (currentDirection != Direction.DOWN) {onDirectionChanged(Direction.UP)} },
            modifier = buttonModifiers
        ) {
            Icon(Icons.Default.KeyboardArrowUp, null)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = { if (currentDirection != Direction.RIGHT) {onDirectionChanged(Direction.LEFT)} },
                modifier = buttonModifiers
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, null)
            }
            Spacer(modifier=buttonModifiers)
            Button(
                onClick = { if (currentDirection != Direction.LEFT) {onDirectionChanged(Direction.RIGHT)} },
                modifier = buttonModifiers
            ) {
                Icon(Icons.Default.KeyboardArrowRight, null)
            }
        }
        Button(
            onClick = { if (currentDirection != Direction.UP) {onDirectionChanged(Direction.DOWN)} },
            modifier = buttonModifiers
        ) {
            Icon(Icons.Default.KeyboardArrowDown, null)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GamePreview() {
    SnakeTheme {
        GameScreen(navController = rememberNavController())
    }
}

