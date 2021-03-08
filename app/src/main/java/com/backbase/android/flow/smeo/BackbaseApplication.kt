package com.backbase.android.flow.smeo

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.backbase.android.Backbase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class BackbaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        configureBackbase(applicationContext)

        startKoin {
            androidContext(applicationContext)
            loadKoinModules(applicationModule)
        }
    }

    private fun configureBackbase(context: Context){
        Backbase.initialize(context,
            BACKBASE_CONFIG_PATH, false)
    }

    companion object {
        const val BACKBASE_CONFIG_PATH = "backbase/conf/config.json"
    }
}