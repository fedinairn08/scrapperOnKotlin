package org.scrapper.service.jpaImpl

import lombok.RequiredArgsConstructor
import org.scrapper.entity.GitHubInfo
import org.scrapper.entity.Link
import org.scrapper.repository.jpa.JpaGitHubInfoRepository
import org.scrapper.service.GitHubInfoService

@RequiredArgsConstructor
class JpaGitHubInfoService(private val jpaGitHubInfoRepository: JpaGitHubInfoRepository): GitHubInfoService {
    override fun add(link: Link) {
        val gitHubInfo = GitHubInfo()
        gitHubInfo.link = link
        jpaGitHubInfoRepository.save(gitHubInfo)
    }

    override fun find(linkId: Long): GitHubInfo {
        return jpaGitHubInfoRepository.findByLink_Id(linkId)
    }

    override fun update(id: Long, lastCommitCount: Int, lastBranchCount: Int) {
        jpaGitHubInfoRepository.save(
            jpaGitHubInfoRepository.findById(id)
                .orElseThrow { RuntimeException("Link with id [$id] not found") }
                .apply {
                    this.lastCommitCount = lastCommitCount
                    this.lastBranchCount = lastBranchCount
                }
        )
    }
}