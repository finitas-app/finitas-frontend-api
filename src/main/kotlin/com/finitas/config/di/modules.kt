package com.finitas.config.di

import com.finitas.adapters.*
import com.finitas.config.urls.*
import com.finitas.domain.ports.*
import com.finitas.domain.services.*
import org.koin.dsl.module

fun urlsModule(profile: Profile) = module {
    single<UrlProvider> {
        when(profile) {
            Profile.prod -> ProductionUrls
            Profile.dev -> DevelopmentUrls
            Profile.docker -> DockerUrls
        }
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
    single<UserStoreRepository> { UserStoreRepositoryImpl(get()) }
    single<AuthService> { AuthService(get(), get()) }
}

val roomModule = module {
    single<RoomMessageRepository> { RoomMessageRepositoryImpl(get()) }
    single<RoomRepository> { RoomRepositoryImpl(get()) }
    single { RoomMessageService(get(), get()) }
    single { RoomService(get()) }
}

val notificationModule = module {
    single<UserNotifierPort> { UserNotifierAdapter() }
}
