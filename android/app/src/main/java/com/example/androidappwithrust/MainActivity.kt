package com.example.androidappwithrust

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidappwithrust.ui.theme.AndroidAppWithRustTheme
import kotlin.system.measureNanoTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppWithRustTheme {
                PerformanceScreen()
            }
        }
    }
}

@Composable
fun PerformanceScreen() {
    var num by remember { mutableStateOf(TextFieldValue("5")) }
    var kotlinPrimeResult by remember { mutableStateOf<String?>(null) }
    var rustPrimeResult by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<String?>(null) }
    var winner by remember { mutableStateOf<String?>(null) }
    var winnerImageRes by remember { mutableStateOf<Int?>(null) }

    val rustLib = RustLib()
    val kotlinLib = KotlinLib()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NumberInputField(num) { num = it }
        Spacer(modifier = Modifier.height(16.dp))
        PerformanceTestButton(
            num = num,
            rustLib = rustLib,
            kotlinLib = kotlinLib,
            onResult = { kotlinResult, rustResult, performanceResult, winnerName, winnerImage ->
                kotlinPrimeResult = kotlinResult
                rustPrimeResult = rustResult
                result = performanceResult
                winner = winnerName
                winnerImageRes = winnerImage
            }
        )
        PerformanceResults(
            kotlinPrimeResult = kotlinPrimeResult,
            rustPrimeResult = rustPrimeResult,
            result = result,
            winner = winner,
            winnerImageRes = winnerImageRes
        )
    }
}

@Composable
fun NumberInputField(num: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    TextField(
        value = num,
        onValueChange = onValueChange,
        label = { Text("Number") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PerformanceTestButton(
    num: TextFieldValue,
    rustLib: RustLib,
    kotlinLib: KotlinLib,
    onResult: (String?, String?, String?, String?, Int?) -> Unit
) {
    Button(
        onClick = {
            val n = num.text.toIntOrNull() ?: 0
            val kotlinResult = if (kotlinLib.isPrime(n)) "$n is a prime number" else "$n is not a prime number"
            val rustResult = if (rustLib.isPrime(n)) "$n is a prime number" else "$n is not a prime number"

            val kotlinTime = measureNanoTime { kotlinLib.isPrime(n) }
            val rustTime = measureNanoTime { rustLib.isPrime(n) }

            val performanceResult = "Kotlin time: $kotlinTime ns, Rust time: $rustTime ns"

            val (winnerName, winnerImage) = when {
                kotlinTime < rustTime -> "Kotlin Win!!!" to R.drawable.android_head_flat
                rustTime < kotlinTime -> "Rust Win!!!" to R.drawable.rust_logo_blk
                else -> "It's a tie!!!" to null
            }

            onResult(kotlinResult, rustResult, performanceResult, winnerName, winnerImage)
        }
    ) {
        Text("Run Performance Test")
    }
}

@Composable
fun PerformanceResults(
    kotlinPrimeResult: String?,
    rustPrimeResult: String?,
    result: String?,
    winner: String?,
    winnerImageRes: Int?
) {
    kotlinPrimeResult?.let {
        Text("Kotlin: $it")
        Spacer(modifier = Modifier.height(8.dp))
    }

    rustPrimeResult?.let {
        Text("Rust: $it")
        Spacer(modifier = Modifier.height(8.dp))
    }

    result?.let {
        Text(it)
        Spacer(modifier = Modifier.height(16.dp))
    }

    winner?.let {
        Text(it, style = MaterialTheme.typography.labelLarge)
    }

    winnerImageRes?.let {
        Image(painter = painterResource(id = it), contentDescription = "Winner Image")
    }
}

@Preview(showBackground = true)
@Composable
fun PerformanceScreenPreview() {
    AndroidAppWithRustTheme {
        PerformanceScreen()
    }
}