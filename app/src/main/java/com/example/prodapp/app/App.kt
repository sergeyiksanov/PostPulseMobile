package com.example.prodapp.app

import android.app.Application
import com.example.prodapp.di.appModule
import com.example.prodapp.di.dataModule
import com.example.prodapp.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
//            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(appModule, domainModule, dataModule))
        }
    }

}