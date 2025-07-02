package org.bot.configuration

import com.pengrad.telegrambot.UpdatesListener
import lombok.RequiredArgsConstructor
import org.bot.handler.MessageHandler
import org.bot.tg.Bot
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class CommandLineRunnerImpl(private val bot: Bot, private val messageHandler: MessageHandler): CommandLineRunner {
    override fun run(vararg args: String?) {
        bot.registerCommands()
        bot.telegramBot.setUpdatesListener { updates ->
            for (update in updates) {
                messageHandler.handleMessage(update)
            }
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }
}