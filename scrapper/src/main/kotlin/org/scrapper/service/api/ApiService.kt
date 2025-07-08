package org.scrapper.service.api

import org.linkparser.model.LinkParserResult
import org.scrapper.entity.Link

abstract class ApiService {

    protected lateinit var nextService: ApiService

    fun setNextService(nextService: ApiService): ApiService {
        this.nextService = nextService
        return nextService
    }

    abstract fun checkUpdate(linkParserResult: LinkParserResult, link: Link): String
}