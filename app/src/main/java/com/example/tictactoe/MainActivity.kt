package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeGame()
        }
    }
}

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var isPlayerTurn by remember { mutableStateOf(true) }
    var winner by remember { mutableStateOf<String?>(null) }
    var movesLeft by remember { mutableStateOf(9) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tic Tac Toe", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))

        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                            .clickable {
                                if (board[i][j].isEmpty() && winner == null) {
                                    board[i][j] = "X"
                                    movesLeft--
                                    winner = checkWinner(board)
                                    isPlayerTurn = false
                                    if (winner == null && movesLeft > 0) {
                                        val (aiRow, aiCol) = bestMove(board)
                                        board[aiRow][aiCol] = "O"
                                        movesLeft--
                                        winner = checkWinner(board)
                                        isPlayerTurn = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(board[i][j], fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))
        winner?.let {
            Text(text = "$it Wins!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            board = List(3) { MutableList(3) { "" } }
            isPlayerTurn = true
            winner = null
            movesLeft = 9
        }) {
            Text("Restart")
        }
    }
}

fun checkWinner(board: List<List<String>>): String? {
    for (i in 0..2) {
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0].isNotEmpty()) return board[i][0]
        if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i].isNotEmpty()) return board[0][i]
    }
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0].isNotEmpty()) return board[0][0]
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2].isNotEmpty()) return board[0][2]
    return null
}

fun bestMove(board: List<List<String>>): Pair<Int, Int> {
    var bestScore = Int.MIN_VALUE
    var move = Pair(0, 0)
    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j].isEmpty()) {
                val newBoard = board.map { it.toMutableList() }
                newBoard[i][j] = "O"
                val score = minimax(newBoard, false)
                if (score > bestScore) {
                    bestScore = score
                    move = Pair(i, j)
                }
            }
        }
    }
    return move
}

fun minimax(board: List<List<String>>, isMaximizing: Boolean): Int {
    val winner = checkWinner(board)
    when (winner) {
        "X" -> return -10
        "O" -> return 10
    }
    if (board.flatten().none { it.isEmpty() }) return 0

    if (isMaximizing) {
        var bestScore = Int.MIN_VALUE
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    val newBoard = board.map { it.toMutableList() }
                    newBoard[i][j] = "O"
                    val score = minimax(newBoard, false)
                    bestScore = max(score, bestScore)
                }
            }
        }
        return bestScore
    } else {
        var bestScore = Int.MAX_VALUE
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    val newBoard = board.map { it.toMutableList() }
                    newBoard[i][j] = "X"
                    val score = minimax(newBoard, true)
                    bestScore = min(score, bestScore)
                }
            }
        }
        return bestScore
    }
}
