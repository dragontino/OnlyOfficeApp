package com.onlyoffice.app

import android.app.Application
import com.onlyoffice.app.di.appModule
import com.onlyoffice.data.di.dataModule
import com.onlyoffice.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger

class OfficeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@OfficeApplication)
            slf4jLogger()
            modules(domainModule, dataModule, appModule)
        }
    }
}