package com.hfad.tagalong.tools.api

import android.util.Base64
import com.google.gson.Gson
import com.hfad.tagalong.BuildConfig
import com.hfad.tagalong.tools.api.config.ContentType
import com.hfad.tagalong.tools.api.config.Endpoint
import com.hfad.tagalong.tools.api.config.Host
import com.hfad.tagalong.tools.api.types.RefreshTokenResponse
import com.hfad.tagalong.tools.api.types.TokenResponse
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

object TokenManager {
    private val client = OkHttpClient()
    private const val CLIENT_ID = BuildConfig.CLIENT_ID
    private lateinit var REFRESH_TOKEN: String
    private var TOKEN: String? = null
    private lateinit var EXPIRY_DATE: Date
    private lateinit var codeVerifier: String

    private const val RESPONSE_TYPE = "code"
    private const val REDIRECT_URI = "appscheme://tagalong-app.com"
    private const val CODE_CHALLENGE_METHOD = "S256"
    private val SCOPES = arrayOf(
        "playlist-read-private",
        "playlist-read-collaborative",
        "playlist-modify-public",
        "playlist-modify-private",
        "user-library-read"
    )

    fun isUserNotLoggedIn(): Boolean = !this::REFRESH_TOKEN.isInitialized

    fun getToken(): String? {
        if (TOKEN == null || EXPIRY_DATE.before(Date())) {
            refreshToken()
        }
        return TOKEN
    }

    fun generateTokenFromCode(code: String) {
        val body = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", CLIENT_ID)
            .add("code", code)
            .addEncoded("redirect_uri", REDIRECT_URI)
            .add("code_verifier", codeVerifier)
            .build()

        val request = Request.Builder()
            .url(
                URLBuilder()
                    .from(Host.ACCOUNTS, Endpoint.TOKEN)
                    .build()
            )
            .header("Content-Type", ContentType.CONTENT_TYPE_XFORM)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw java.io.IOException("Unexpected code $response") // TODO: Manejar errores
            val gson = Gson().fromJson(response.body?.string(), TokenResponse::class.java)
            TOKEN = gson.access_token
            REFRESH_TOKEN = gson.refresh_token
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.add(Calendar.SECOND, gson.expires_in)
            EXPIRY_DATE = calendar.time
        }
    }

    fun getAuthSpotifyURL(): String {
        generateCodeVerifier()
        val codeChallenge = getCodeChallenge()
        return URLBuilder()
            .from(Host.ACCOUNTS, Endpoint.AUTHORIZE)
            .param("client_id", CLIENT_ID)
            .param("response_type", RESPONSE_TYPE)
            .param("redirect_uri", REDIRECT_URI)
            .param("code_challenge_method", CODE_CHALLENGE_METHOD)
            .param("code_challenge", codeChallenge)
            .param("scope", SCOPES.joinToString(","))
            .build()
    }

    // Source: https://auth0.com/docs/flows/call-your-api-using-the-authorization-code-flow-with-pkc
    private fun generateCodeVerifier() {
        val sr = SecureRandom()
        val code = ByteArray(32)
        sr.nextBytes(code)
        codeVerifier = Base64.encodeToString(
            code,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }

    private fun getCodeChallenge(): String {
        val bytes: ByteArray = codeVerifier.toByteArray(Charset.forName("US-ASCII"))
        val md = MessageDigest.getInstance("SHA-256")
        md.update(bytes, 0, bytes.size)
        val digest = md.digest()
        return Base64.encodeToString(
            digest,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }

    private fun refreshToken() {
        val body = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("client_id", CLIENT_ID)
            .add("refresh_token", REFRESH_TOKEN)
            .build()

        val request = Request.Builder()
            .url(
                URLBuilder()
                    .from(Host.ACCOUNTS, Endpoint.TOKEN)
                    .build()
            )
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