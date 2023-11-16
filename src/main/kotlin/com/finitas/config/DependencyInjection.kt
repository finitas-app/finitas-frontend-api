package com.finitas.config

import com.finitas.adapters.services.ReceiptService
import org.koin.dsl.module

val appModule = module {
    single<ReceiptService> { ReceiptService() }
}
