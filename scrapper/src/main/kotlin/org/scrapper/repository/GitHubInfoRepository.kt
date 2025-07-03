package org.scrapper.repository

import org.scrapper.entity.GitHubInfo

interface GitHubInfoRepository {

    fun save(gitHubInfo: GitHubInfo)

    fun find(linkId: Long): GitHubInfo?

    fun update(id: Long, lastCommitCount: Int, lastBranchCount: Int)
}