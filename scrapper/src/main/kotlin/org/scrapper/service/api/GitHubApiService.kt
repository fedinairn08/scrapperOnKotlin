package org.scrapper.service.api

import lombok.RequiredArgsConstructor
import org.linkparser.model.GitHubResult
import org.linkparser.model.LinkParserResult
import org.scrapper.client.GitHubClient
import org.scrapper.dto.response.GitHubRepositoryResponse
import org.scrapper.entity.GitHubInfo
import org.scrapper.entity.Link
import org.scrapper.service.GitHubInfoService
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class GitHubApiService(
    private val gitHubClient: GitHubClient,
    private val gitHubInfoService: GitHubInfoService
): ApiService() {
    override fun checkUpdate(linkParserResult: LinkParserResult, link: Link): String {
        if (linkParserResult is GitHubResult) {
            val response: GitHubRepositoryResponse? = gitHubClient.getGitHubRepositoryInfo(
                linkParserResult.user,
                linkParserResult.repo
            )

            var commitCount = gitHubClient.getCommitCount(linkParserResult.user, linkParserResult.repo)
            var branchCount = gitHubClient.getBranchCount(linkParserResult.user, linkParserResult.repo)

            val gitHubInfo: GitHubInfo = gitHubInfoService.find(link.id!!)

            if (gitHubInfo.lastCommitCount == 0 && gitHubInfo.lastBranchCount == 0) {
                gitHubInfo.lastCommitCount = commitCount
                gitHubInfo.lastBranchCount = branchCount

                gitHubInfoService.update(gitHubInfo.id!!, commitCount, branchCount)
            } else {
                commitCount -= gitHubInfo.lastCommitCount
                branchCount -= gitHubInfo.lastBranchCount
            }

            return ((("Обновление [репозитория](" +
                    linkParserResult.url) +
                    ")\n" +
                    "Последнее обновление: " +
                    response!!.updatedAt.toString()) +
                    "\n" +
                    "Последний push: " +
                    response.pushedAt.toString()) +
                    "\n" +
                    "Количество новых коммитов: " +
                    commitCount +
                    "\n" +
                    "Количество новых веток: " +
                    branchCount +
                    "\n"
        } else {
            return nextService.checkUpdate(linkParserResult, link)
        }
    }
}