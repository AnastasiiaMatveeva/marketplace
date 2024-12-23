package ru.otus.otuskotlin.aiassistant.app.rabbit.config

import ru.otus.otuskotlin.aiassistant.app.common.IAppSettings
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.common.CorSettings

data class AppSettings(
    override val corSettings: CorSettings = CorSettings(),
    override val processor: ModelProcessor = ModelProcessor(corSettings),
    override val rabbit: RabbitConfig = RabbitConfig(),
    override val controllersConfigV1: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
    override val controllersConfigV2: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
): IAppSettings, IAppRabbitSettings
