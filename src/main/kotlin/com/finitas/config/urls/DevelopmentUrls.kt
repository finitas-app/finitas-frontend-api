package com.finitas.config.urls

object DevelopmentUrls : UrlProvider {
    override val RECEIPT_SERVICE_HOST_URL = "http://192.168.1.32:8080"
    override val RECEIPT_PARSING_TIMOUT = 8000L
}