package com.finitas.config.web

import com.finitas.adapters.KredsSubscription
import com.finitas.adapters.userEventChannelName
import com.finitas.config.urls.UrlProvider
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.KredsConnectionException
import io.github.crackthecodeabhi.kreds.connection.newSubscriberClient
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject

fun Application.configureRedis() {
    val urlProvider: UrlProvider by inject()

    launch(Dispatchers.Default) {
        newSubscriberClient(Endpoint.from(urlProvider.REDIS_HOST_URL), KredsSubscription).use { redisClient ->
            while (isActive) {
                try {
                    redisClient.subscribe(userEventChannelName)
                    while (isActive) {
                        redisClient.ping()
                        delay(5000)
                    }
                } catch (e: KredsConnectionException) {
                    log.error("Subscription is not working. Redis is not available.")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(10000)
            }
        }
    }
}