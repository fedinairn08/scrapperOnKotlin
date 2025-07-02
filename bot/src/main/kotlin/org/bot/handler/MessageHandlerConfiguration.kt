package org.bot.handler

import lombok.RequiredArgsConstructor
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@RequiredArgsConstructor
class MessageHandlerConfiguration(private val context: ApplicationContext) {

    @Bean
    fun messageHandler(): MessageHandler {
        val reflections = Reflections(ClasspathHelper.forClass(MessageHandler::class.java))

        val handlerClasses = reflections.getSubTypesOf(MessageHandler::class.java)
                .filter { it.kotlin.isAbstract.not() && it.kotlin.objectInstance == null }
                .toMutableList()

        handlerClasses.remove(StartCommandHandler::class.java)
        handlerClasses.remove(DefaultHandler::class.java)

        val startHandler = context.getBean(StartCommandHandler::class.java)
        var currentHandler: MessageHandler = startHandler

        handlerClasses.forEach { handlerClass ->
            val nextHandler = context.getBean(handlerClass)
            currentHandler.setNextHandler(nextHandler)
            currentHandler = nextHandler
        }

        currentHandler.setNextHandler(context.getBean(DefaultHandler::class.java))

        return startHandler
    }
}