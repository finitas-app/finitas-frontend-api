package com.finitas.config.di

import com.finitas.adapters.*
import com.finitas.config.urls.DevelopmentUrls
import com.finitas.config.urls.ProductionUrls
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.ports.*
import com.finitas.domain.services.*
import org.koin.dsl.module

fun urlsModule(isDevelopment: Boolean) = module {
    single<UrlProvider> {
        if (isDevelopment)
            DevelopmentUrls
        else
            ProductionUrls
    }
}

val storeModule = module {
    single<FinishedSpendingStoreRepository> { FinishedSpendingStoreRepositoryImpl(get()) }
    single<ShoppingListStoreRepository> { ShoppingListStoreRepositoryImpl(get()) }
    single<UserStoreRepository> { UserStoreRepositoryImpl(get()) }

    single<UserRoleRepository> { UserRoleRepositoryImpl() }

    single<FinishedSpendingStoreService> { FinishedSpendingStoreService(get()) }
    single<ShoppingListStoreService> { ShoppingListStoreService(get()) }
    single<UserStoreService> { UserStoreService(get()) }

    single<UserRoleService> { UserRoleService(get()) }
}

val receiptModule = module {
    single<ReceiptRepository> { ReceiptRepositoryImpl(get()) }
    single<ReceiptService> { ReceiptService(get()) }
}

val authModule = module {
    single<AuthRepository> { AuthZeroRepositoryImpl(get()) }
    single<AuthService> { AuthService(get()) }
}
