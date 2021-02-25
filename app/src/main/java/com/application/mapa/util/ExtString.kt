package com.application.mapa.util

fun String.contains(chars: CharArray): Boolean {
    chars.forEach {
        if (contains(it)) {
            return true
        }
    }
    return false
}