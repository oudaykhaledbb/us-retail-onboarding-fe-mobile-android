package com.backbase.android.flow.smeo

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.backbase.android.Backbase

import com.backbase.android.core.utils.BBLogger
import com.backbase.android.flow.common.logging.TAG
import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.google.gson.internal.LinkedTreeMap
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

    private fun configureBackbase(context: Context) {
        Backbase.initialize(
            context,
            BACKBASE_CONFIG_PATH, false
        ).also {
            setHttpHeaders()
        }
    }

    fun setHttpHeaders() {
        // TODO: Update global bypass header
        // This header is required to sign in through identity, bypassing VPN
        val configHeader = Backbase.requireInstance().configuration.custom["default-http-headers"]
        val bypassHeader = tryCast(configHeader)
        NetworkConnectorBuilder.Configurations.appendHeaders(bypassHeader)
    }

    private fun tryCast(any: Any?): HashMap<String, String> {

        val hashMap: HashMap<String, String> = HashMap()
        try {
            val map = any as LinkedTreeMap<*, *>
            for ((k, v) in map) {
                val stringK = k as String
                val stringV = v as String
                hashMap[stringK] = stringV
            }
        } catch (e: Exception) {
            BBLogger.error(TAG, "Failed to cast header values")
        }
        return hashMap
    }

    companion object {
        const val BACKBASE_CONFIG_PATH = "backbase/conf/config.json"
    }
}