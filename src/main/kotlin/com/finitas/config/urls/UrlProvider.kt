package com.finitas.config.urls

interface UrlProvider {
    val RECEIPT_SERVICE_HOST_URL: String
    val AUTH0_CLIENT_ID: String
    val AUTH0_CLIENT_SECRET: String
    val AUTH0_FINITAS_API_AUDIENCE: String
    val AUTH0_DOMAIN: String
}