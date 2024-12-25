package ru.otus.otuskotlin.aiassistant.common

import ru.otus.otuskotlin.aiassistant.common.ws.IWsSessionRepo
import ru.otus.otuskotlin.aiassistant.logging.common.LoggerProvider

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE,

    ) {
    companion object {
        val NONE = CorSettings()
    }
}
