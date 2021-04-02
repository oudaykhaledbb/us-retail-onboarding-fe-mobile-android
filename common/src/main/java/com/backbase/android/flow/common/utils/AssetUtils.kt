package com.backbase.android.flow.common.utils

import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

fun readAsset(mgr: AssetManager, path: String?): String? {
    var contents = ""
    var inputStream: InputStream? = null
    var reader: BufferedReader? = null
    try {
        inputStream = mgr.open(path!!)
        reader = BufferedReader(InputStreamReader(inputStream))
        contents = reader.readLine()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            contents += """
                
                $line
                """.trimIndent()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (inputStream != null) {
            try {
                inputStream.close()
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