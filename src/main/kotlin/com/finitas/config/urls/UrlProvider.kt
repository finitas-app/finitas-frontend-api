package com.finitas.config.urls

interface UrlProvider {
    val RECEIPT_SERVICE_HOST_URL: String
    val RECEIPT_PARSING_TIMEOUT: Long
}