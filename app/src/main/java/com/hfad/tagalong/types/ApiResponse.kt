package com.hfad.tagalong.types

data class ApiResponse<T> (
    val statusCode: Int,
    val result: T?
) {
    val success: Boolean
        get() = SUCCESS_STATUS_CODES.indexOf(statusCode) != -1


    private companion object {
        val SUCCESS_STATUS_CODES = arrayOf(200, 201)
    }
}