package com.finitas.config.urls

interface UrlProvider {
    val RECEIPT_SERVICE_HOST_URL: String
    val AUTH0_CLIENT_ID: String
    val AUTH0_CLIENT_SECRET: String
    val AUTH0_FINITAS_API_AUDIENCE: String
    val AUTH0_DOMAIN: String
    val RECEIPT_PARSING_TIMEOUT: Long
    val STORE_HOST_URL: String
    val ROOM_MANAGER_HOST_URL: String
}