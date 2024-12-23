package ru.otus.otuskotlin.aiassistant.app.spring.config

import ru.otus.otuskotlin.aiassistant.app.common.IAppSettings
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.common.CorSettings

data class AppSettings(
    override val corSettings: CorSettings,
    override val processor: ModelProcessor,
): IAppSettings
