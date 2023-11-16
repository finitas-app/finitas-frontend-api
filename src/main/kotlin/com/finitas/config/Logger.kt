package com.finitas.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

class Logger {
    private var logger: Logger? = null

    operator fun getValue(thisRef: Any, property: KProperty<*>): Logger =
        logger ?: synchronized(this) {
            logger ?: LoggerFactory.getLogger(thisRef::class.java).also { logger = it }
        }
}