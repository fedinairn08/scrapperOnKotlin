package org.scrapper.service.jdbcImpl

import lombok.RequiredArgsConstructor
import org.scrapper.entity.GitHubInfo
import org.scrapper.entity.Link
import org.scrapper.repository.GitHubInfoRepository
import org.scrapper.service.GitHubInfoService

@RequiredArgsConstructor
class JdbcGitHubInfoService(private val gitHubInfoRepository: GitHubInfoRepository): GitHubInfoService {
    override fun add(link: Link) {
        val gitHubInfo = GitHubInfo()
        gitHubInfo.link = link
        gitHubInfoRepository.save(gitHubInfo)
    }

    override fun find(linkId: Long): GitHubInfo {
        return gitHubInfoRepository.find(linkId)!!
    }

    override fun update(id: Long, lastCommitCount: Int, lastBranchCount: Int) {
        gitHubInfoRepository.update(id, lastCommitCount, lastBranchCount)
    }
}