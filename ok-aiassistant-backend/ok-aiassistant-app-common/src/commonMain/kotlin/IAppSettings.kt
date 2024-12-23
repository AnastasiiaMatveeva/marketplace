package ru.otus.otuskotlin.aiassistant.app.common

import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.common.CorSettings

interface IAppSettings {
    val processor: ModelProcessor
    val corSettings: CorSettings
}
