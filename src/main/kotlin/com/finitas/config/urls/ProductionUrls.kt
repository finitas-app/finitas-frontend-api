package com.finitas.config.urls

object ProductionUrls : UrlProvider {
    override val RECEIPT_SERVICE_HOST_URL = "http://localhost:8080/parse"
    override val RECEIPT_PARSING_TIMEOUT = 1000L
}