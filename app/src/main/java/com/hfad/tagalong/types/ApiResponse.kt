package com.hfad.tagalong.types

data class ApiResponse<T> (
    val statusCode: Int,
    val result: T?
)