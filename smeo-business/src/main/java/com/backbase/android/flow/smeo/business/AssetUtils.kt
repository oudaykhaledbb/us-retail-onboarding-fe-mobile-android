package com.backbase.android.flow.smeo.business

import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

fun readAsset(mgr: AssetManager, path: String?): String? {
    var contents = ""
    var `is`: InputStream? = null
    var reader: BufferedReader? = null
    try {
        `is` = mgr.open(path!!)
        reader = BufferedReader(InputStreamReader(`is`))
        contents = reader.readLine()
        var line: String? = null
        while (reader.readLine().also({ line = it }) != null) {
            contents += """
                
                $line
                """.trimIndent()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (`is` != null) {
            try {
                `is`.close()
            } catch (ignored: IOException) {
            }
        }
        if (reader != null) {
            try {
                reader.close()
            } catch (ignored: IOException) {
            }
        }
    }
    return contents
}