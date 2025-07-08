package org.scrapper.repository.jpa

import org.scrapper.entity.GitHubInfo
import org.scrapper.entity.Link
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JpaGitHubInfoRepository: JpaRepository<GitHubInfo, Long> {

    fun findByLink_Id(linkId: Long): GitHubInfo

    override fun findById(id: Long): Optional<GitHubInfo>

    fun deleteByLink(link: Link)
}