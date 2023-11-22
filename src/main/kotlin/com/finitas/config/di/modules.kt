package com.finitas.config.di

import com.finitas.adapters.AuthZeroRepositoryImpl
import com.finitas.adapters.ReceiptRepositoryImpl
import com.finitas.config.urls.UrlProvider
import com.finitas.config.urls.DevelopmentUrls
import com.finitas.config.urls.ProductionUrls
import com.finitas.domain.ports.AuthRepository
import com.finitas.domain.ports.ReceiptRepository
import com.finitas.domain.services.AuthService
import com.finitas.domain.services.ReceiptService
import org.koin.dsl.module

fun urlsModule(isDevelopment: Boolean) = module {
    single<UrlProvider> {
        if (isDevelopment)
            DevelopmentUrls
        else
            ProductionUrls
    }
}

val receiptModule = module {
    single<ReceiptRepository> { ReceiptRepositoryImpl(get()) }
    single<ReceiptService> { ReceiptService(get()) }
}

val authModule = module {
    single<AuthRepository> { AuthZeroRepositoryImpl(get()) }
    single<AuthService> { AuthService(get()) }
}
