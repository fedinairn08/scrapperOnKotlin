package org.scrapper.exception.customExceptions

class DuplicateChatException(id: Long): RuntimeException("Чат $id уже зарегистрирован")