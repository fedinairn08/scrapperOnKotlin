package org.bot.handler

import com.pengrad.telegrambot.model.Update
import org.bot.tg.Bot

abstract class MessageHandler(protected val bot: Bot) {

    protected var nextHandler: MessageHandler? = null

    fun setNextHandler(nextHandler: MessageHandler): MessageHandler {
        this.nextHandler = nextHandler
        return nextHandler
    }

    abstract fun handleMessage(update: Update)
}