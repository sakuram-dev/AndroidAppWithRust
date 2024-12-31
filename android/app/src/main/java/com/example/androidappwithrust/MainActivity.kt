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
    var kotlinTime by remember { mutableStateOf<Long?>(null) }
    var rustTime by remember { mutableStateOf<Long?>(null) }
    var result by remember { mutableStateOf<String?>(null) }
    var winner by remember { mutableStateOf<String?>(null) }
    var winnerImageRes by remember { mutableStateOf<Int?>(null) }
    var num by remember { mutableStateOf(TextFieldValue("5")) } // 初期値を設定
    var kotlinPrimeResult by remember { mutableStateOf<String?>(null) }
    var rustPrimeResult by remember { mutableStateOf<String?>(null) }

    val rustLib = RustLib()
    val kotlinLib = KotlinLib()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = num,
            onValueChange = { num = it },
            label = { Text("Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // 数字入力キーボードを指定
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val n = num.text.toIntOrNull() ?: 0
                kotlinPrimeResult = if (kotlinLib.isPrime(n)) "$n is a prime number" else "$n is not a prime number"
                rustPrimeResult = if (rustLib.isPrime(n)) "$n is a prime number" else "$n is not a prime number"

                // ウォームアップ
                for (i in 0..100000) {
                    kotlinLib.isPrime(n)
                    rustLib.isPrime(n)
                }

                // 実行時間の測定
                val kotlinDuration = measureNanoTime {
                    for (i in 0..1000000) {
                        kotlinLib.isPrime(n)
                    }
                }
                val rustDuration = measureNanoTime {
                    for (i in 0..1000000) {
                        rustLib.isPrime(n)
                    }
                }
                kotlinTime = kotlinDuration
                rustTime = rustDuration
                result = "Kotlin time: $kotlinDuration ns, Rust time: $rustDuration ns"

                // 結果を比較して勝者を決定
                if (kotlinDuration < rustDuration) {
                    winner = "Kotlin Win!!!"
                    winnerImageRes = R.drawable.android_head_flat
                } else if (rustDuration < kotlinDuration) {
                    winner = "Rust Win!!!"
                    winnerImageRes = R.drawable.rust_logo_blk
                } else {
                    winner = "It's a tie!!!"
                    winnerImageRes = null
                }
            }
        ) {
            Text("Run Performance Test")
        }

        Spacer(modifier = Modifier.height(16.dp))

        kotlinPrimeResult?.let {
            Text("Kotlin: $it")
        }

        Spacer(modifier = Modifier.height(8.dp))

        rustPrimeResult?.let {
            Text("Rust: $it")
        }

        Spacer(modifier = Modifier.height(8.dp))

        result?.let {
            Text(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        winner?.let {
            Text(it, style = MaterialTheme.typography.labelLarge)
        }

        winnerImageRes?.let {
            Image(painter = painterResource(id = it), contentDescription = "Winner Image")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerformanceScreenPreview() {
    AndroidAppWithRustTheme {
        PerformanceScreen()
    }
}