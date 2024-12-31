// app/src/main/java/com/example/androidappwithrust/RustLib.kt
package com.example.androidappwithrust

class RustLib {
    companion object {
        init {
            System.loadLibrary("rust_lib") // Rustライブラリをロード
        }
    }

    external fun isPrime(a: Int): Boolean
}