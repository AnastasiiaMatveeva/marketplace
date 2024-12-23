package ru.otus.otuskotlin.aiassistant.common

import ru.otus.otuskotlin.aiassistant.logging.common.LoggerProvider

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
) {
    companion object {
        val NONE = CorSettings()
    }
}
