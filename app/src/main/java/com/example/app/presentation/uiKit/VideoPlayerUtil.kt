package com.example.app.presentation.uiKit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class VideoPlayerUtil(private val scope: CoroutineScope) {

    @Throws(IOException::class)
    suspend fun downloadUrl(myurl: String?): String? {
        var result = ""
        scope.launch {
            var inputStream: InputStream? = null
            result = try {
                val url = URL(myurl)
                val conn =
                    withContext(Dispatchers.IO) {
                        url.openConnection()
                    } as HttpURLConnection
                conn.readTimeout = 10000
                conn.connectTimeout = 15000
                conn.requestMethod = "GET"
                conn.doInput = true
                withContext(Dispatchers.IO) {
                    conn.connect()
                }
                inputStream = conn.inputStream
                readIt(inputStream)
            } finally {
                inputStream?.close()
            }
        }
        return result
    }

    @Throws(IOException::class)
    private fun readIt(stream: InputStream?): String {
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        val sb = StringBuilder()
        var line: String
        while (reader.readLine().also { line = it } != null) {
            if (line.contains("fmt_stream_map")) {
                sb.append(
                    """
                    $line

                    """.trimIndent()
                )
                break
            }
        }
        reader.close()
        val result: String = decode(sb.toString())
        val url = result.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        return url[1]
    }

    private fun decode(streamString: String): String {
        var working = streamString
        var index: Int
        index = working.indexOf("\\u")
        while (index > -1) {
            val length = working.length
            if (index > length - 6) break
            val numStart = index + 2
            val numFinish = numStart + 4
            val substring = working.substring(numStart, numFinish)
            val number = substring.toInt(16)
            val stringStart = working.substring(0, index)
            val stringEnd = working.substring(numFinish)
            working = stringStart + number.toChar() + stringEnd
            index = working.indexOf("\\u")
        }
        return working
    }
}