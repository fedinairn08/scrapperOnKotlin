package org.bot.exception

import org.bot.dto.ApiErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception, request: WebRequest): ResponseEntity<ApiErrorResponse?>? {
        return ResponseEntity.badRequest().body<ApiErrorResponse>(
                ApiErrorResponse(
                        request.getDescription(false),
                        HttpStatus.BAD_REQUEST.toString(),
                        ex.javaClass.name,
                        ex.message?: "No message",
                        ex.stackTrace.map { it.toString() }.toList()
                )
        )
    }
}