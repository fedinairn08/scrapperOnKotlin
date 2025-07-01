package org.scrapper.exception.customExceptions

class LinkNotFoundException(url: String): RuntimeException("Ссылка $url не найдена")