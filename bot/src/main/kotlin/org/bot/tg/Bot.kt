package org.bot.tg

import com.pengrad.telegrambot.Callback
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.response.BaseResponse
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException

@Component
@RequiredArgsConstructor
class Bot(val telegramBot: TelegramBot) {

    private val log = LoggerFactory.getLogger(Bot::class.java)

    fun <T : BaseRequest<T, R>?, R : BaseResponse?> send(request: T) {
        telegramBot.execute<T, R>(request, object : Callback<T, R> {
            override fun onResponse(t: T, r: R) {
                log.info("Успешный ответ на запрос: {}", t)
            }

            override fun onFailure(t: T, e: IOException) {
                println("onFailure")
                log.error("Ошибка при выполнении запроса {}: {}", t, e.message, e)
            }
        })
    }
}