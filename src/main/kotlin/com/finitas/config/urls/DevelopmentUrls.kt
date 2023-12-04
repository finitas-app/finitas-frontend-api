package com.finitas.config.urls

object DevelopmentUrls : UrlProvider {
    override val AUTH0_CLIENT_ID = "vI5UfVXjYzJDrpR9fWy6wY6oWe0gh4io"
    override val AUTH0_CLIENT_SECRET = "j_SW-RnFCLiaHZg09-962P_tyin-bslEvP8XBAunb9_kveorScGEcgowULbHAXxw"
    override val AUTH0_FINITAS_API_AUDIENCE = "https://api.finitas.com"
    override val AUTH0_DOMAIN = "https://dev-ktqlmp7ts80r786t.us.auth0.com"
    override val RECEIPT_SERVICE_HOST_URL = "http://localhost:8081/parse"
    override val RECEIPT_PARSING_TIMEOUT = 8000L
}