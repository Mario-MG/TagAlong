package com.hfad.tagalong.tools.api

import com.google.gson.Gson
import com.hfad.tagalong.BuildConfig
import com.hfad.tagalong.tools.api.config.*
import com.hfad.tagalong.tools.api.types.RefreshTokenResponse
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

object TokenManager {
    private val client = OkHttpClient()
    private const val CLIENT_ID = BuildConfig.CLIENT_ID
    private const val CLIENT_SECRET = BuildConfig.CLIENT_SECRET
    private val REFRESH_TOKEN = BuildConfig.REFRESH_TOKEN
    private var TOKEN: String? = null
    private lateinit var EXPIRY_DATE: Date

    fun getToken(): String? {
        if (TOKEN == null || EXPIRY_DATE.before(Date())) {
            refreshToken()
        }
        return TOKEN
    }

    private fun refreshToken() {
        val body = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("client_id", CLIENT_ID)
            .add("client_secret", CLIENT_SECRET)
            .add("refresh_token", REFRESH_TOKEN)
            .build()

        val request = Request.Builder()
            .url(URLBuilder()
                .from(Host.ACCOUNTS, Endpoint.TOKEN)
                .build())
            .header("Content-Type", ContentType.CONTENT_TYPE_XFORM)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw java.io.IOException("Unexpected code $response") // TODO: Manejar errores
            val gson = Gson().fromJson(response.body?.string(), RefreshTokenResponse::class.java)
            TOKEN = gson.access_token
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.add(Calendar.SECOND, gson.expires_in)
            EXPIRY_DATE = calendar.time
        }
    }
}