package ru.otus.otuskotlin.aiassistant.app.rabbit

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.aiassistant.app.rabbit.config.AppSettings
import ru.otus.otuskotlin.aiassistant.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.aiassistant.app.rabbit.mappers.fromArgs
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import ru.otus.otuskotlin.aiassistant.logging.common.LoggerProvider
import ru.otus.otuskotlin.aiassistant.logging.jvm.mpLoggerLogback

fun main(vararg args: String) = runBlocking {
    val appSettings = AppSettings(
        rabbit = RabbitConfig.fromArgs(*args),
        corSettings = CorSettings(
            loggerProvider = LoggerProvider { mpLoggerLogback(it) }
        )
    )
    val app = RabbitApp(appSettings = appSettings, this)
    app.start()
}
