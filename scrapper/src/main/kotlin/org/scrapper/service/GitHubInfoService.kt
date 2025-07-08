package org.scrapper.service

import org.scrapper.entity.GitHubInfo
import org.scrapper.entity.Link

interface GitHubInfoService {

    fun add(link: Link)

    fun find(linkId: Long): GitHubInfo

    fun update(id: Long, lastCommitCount: Int, lastBranchCount: Int)
}