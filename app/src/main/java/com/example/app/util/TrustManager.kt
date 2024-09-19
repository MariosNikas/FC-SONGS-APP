package com.example.app.util

import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Trust all clients
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Trust all servers
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
})
