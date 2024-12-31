package com.example.androidappwithrust

class KotlinLib {
    fun add(a: Int, b: Int): Int {
        return a + b
    }

    fun isPrime(n: Int): Boolean {
        if (n <= 1) return false
        for (i in 2..Math.sqrt(n.toDouble()).toInt()) {
            if (n % i == 0) return false
        }
        return true
    }
}