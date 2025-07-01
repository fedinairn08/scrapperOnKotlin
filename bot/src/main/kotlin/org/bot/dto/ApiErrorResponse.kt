package org.bot.dto

data class ApiErrorResponse(
        var description: String,
        var code: String,
        var exceptionName: String,
        var exceptionMessage: String,
        var stacktrace: List<String>
)
