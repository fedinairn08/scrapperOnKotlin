package org.scrapper.exception.customExceptions

class LinkAlreadyExistsException(url: String): RuntimeException("Ссылка $url уже отслеживается")