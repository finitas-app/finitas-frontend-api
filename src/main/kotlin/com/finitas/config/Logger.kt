package com.finitas.config

object Logger {
    fun info(message: String) {
        println("INFO: $message")
    }
    fun error(message: String, cause: Throwable? = null) {
        println("ERROR: $message, cause: $cause")
    }
}