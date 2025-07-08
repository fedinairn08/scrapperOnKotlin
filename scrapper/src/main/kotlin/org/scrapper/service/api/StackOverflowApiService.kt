package org.scrapper.service.api

import lombok.RequiredArgsConstructor
import org.linkparser.model.LinkParserResult
import org.linkparser.model.StackOverflowResult
import org.scrapper.client.StackOverflowClient
import org.scrapper.dto.response.StackOverflowQuestionResponse
import org.scrapper.dto.stackoverflow.StackOverflowQuestion
import org.scrapper.entity.Link
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class StackOverflowApiService(private val stackOverflowClient: StackOverflowClient): ApiService() {
    override fun checkUpdate(linkParserResult: LinkParserResult, link: Link): String {
        if (linkParserResult is StackOverflowResult) {
            val response: StackOverflowQuestionResponse? = stackOverflowClient.getQuestionInfo(
                linkParserResult.questionId
            )
            val question: StackOverflowQuestion = response!!.items.first()

            return ((("Обновление [вопроса](" +
                    linkParserResult.url) +
                    ")\n" +
                    "Последняя активность: " +
                    question.lastActivityDate.toString()) +
                    "\n" +
                    "Последнее обновление: " +
                    question.lastEditDate.toString()) +
                    "\n" +
                    "На вопрос " +
                    (if (question.isAnswered) "есть ответы" else "нет ответов") +
                    "\n"
        } else {
            return nextService.checkUpdate(linkParserResult, link)
        }
    }
}