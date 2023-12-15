package com.finitas.config.urls

object ProductionUrls : UrlProvider {
    override val RECEIPT_SERVICE_HOST_URL = "http://localhost:8080/api/parse"
    override val AUTH0_CLIENT_ID = ""
    override val AUTH0_CLIENT_SECRET = ""
    override val AUTH0_FINITAS_API_AUDIENCE = ""
    override val AUTH0_DOMAIN = ""
    override val RECEIPT_PARSING_TIMEOUT = 1000L
    override val STORE_HOST_URL = "http://localhost:8081/api/store"
}