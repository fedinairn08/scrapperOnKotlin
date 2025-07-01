package org.scrapper.exception

import org.scrapper.dto.response.ApiErrorResponse
import org.scrapper.exception.customExceptions.ChatNotFoundException
import org.scrapper.exception.customExceptions.DuplicateChatException
import org.scrapper.exception.customExceptions.LinkAlreadyExistsException
import org.scrapper.exception.customExceptions.LinkNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception, request: WebRequest): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.badRequest().body(
                ApiErrorResponse(
                        request.getDescription(false),
                        HttpStatus.BAD_REQUEST.toString(),
                        ex.javaClass.name,
                        ex.message ?: "No message",
                        ex.stackTrace.map { it.toString() }.toList()
                )
        )
    }

    @ExceptionHandler(
            DuplicateChatException::class,
            ChatNotFoundException::class,
            LinkAlreadyExistsException::class,
            LinkNotFoundException::class
    )
    fun handleCustomException(ex: RuntimeException): ResponseEntity<ApiErrorResponse> {
        val status = when (ex) {
            is ChatNotFoundException, is LinkNotFoundException -> HttpStatus.NOT_FOUND
            else -> HttpStatus.BAD_REQUEST
        }

        val error = ApiErrorResponse(
                ex.message?: "No message",
                status.value().toString(),
                ex::class.simpleName ?: "Unknown",
                ex.message ?: "No message",
                emptyList()
        )
        return ResponseEntity(error, status)
    }
}