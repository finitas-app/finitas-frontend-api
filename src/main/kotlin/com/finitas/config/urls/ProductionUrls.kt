package com.finitas.config.urls

object ProductionUrls : UrlProvider {
    override val AUTH0_CLIENT_ID = "vI5UfVXjYzJDrpR9fWy6wY6oWe0gh4io"
    override val AUTH0_CLIENT_SECRET = "j_SW-RnFCLiaHZg09-962P_tyin-bslEvP8XBAunb9_kveorScGEcgowULbHAXxw"
    override val AUTH0_FINITAS_API_AUDIENCE = "https://api.finitas.com"
    override val AUTH0_DOMAIN = "https://dev-ktqlmp7ts80r786t.us.auth0.com"
    override val RECEIPT_SERVICE_HOST_URL = "http://rp-service:8081/api/parse"
    override val RECEIPT_PARSING_TIMEOUT = 30000L
    override val STORE_HOST_URL = "http://fms-service:8082/api/store"
    override val ROOM_MANAGER_HOST_URL = "http://rm-service:8083/api"
    override val REDIS_HOST_URL = "unb-service:6379"
}