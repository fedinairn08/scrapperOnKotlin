package org.scrapper.exception.customExceptions

class ChatNotFoundException(id: Long): RuntimeException("Чат $id не найден")