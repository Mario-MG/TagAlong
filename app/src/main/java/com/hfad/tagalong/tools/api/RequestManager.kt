package com.hfad.tagalong.tools.api

import com.google.gson.Gson
import com.hfad.tagalong.types.ApiResponse
import okhttp3.OkHttpClient
import okhttp3.Request

object RequestManager {
    inline fun <reified T> sendRequest(request: Request): ApiResponse<T>? {
        val client = OkHttpClient()

        client.newCall(request).execute().use { response ->
            val result = if (response.isSuccessful) Gson().fromJson(response.body?.string(), T::class.java) else null // TODO: Mejorar legibilidad
            return ApiResponse(
                response.code,
                result
            )
        }
        return null
    }
}