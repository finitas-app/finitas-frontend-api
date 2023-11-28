package com.finitas.config.urls

object DevelopmentUrls : UrlProvider {
    override val RECEIPT_SERVICE_HOST_URL = "http://localhost:8081/parse"
    override val RECEIPT_PARSING_TIMEOUT = 8000L
}