package ru.otus.otuskotlin.aiassistant.app.rabbit.config

import ru.otus.otuskotlin.aiassistant.app.common.IAppSettings

interface IAppRabbitSettings: IAppSettings {
    val rabbit: RabbitConfig
    val controllersConfigV1: RabbitExchangeConfiguration
    val controllersConfigV2: RabbitExchangeConfiguration
}
