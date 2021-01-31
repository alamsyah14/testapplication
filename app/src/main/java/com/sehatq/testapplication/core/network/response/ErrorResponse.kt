package com.sehatq.testapplication.core.network.response

data class ErrorResponse(
    var code: Int = 500,
    var message: String? = "Internal Server Error."
)