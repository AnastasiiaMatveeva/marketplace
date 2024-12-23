package ru.otus.otuskotlin.aiassistant.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import ru.otus.otuskotlin.aiassistant.logging.common.LoggerProvider
import ru.otus.otuskotlin.aiassistant.logging.jvm.mpLoggerLogback

@Suppress("unused")
@Configuration
class ModelConfig {
    @Bean
    fun processor(corSettings: CorSettings) = ModelProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings(
        corSettings: CorSettings,
        processor: ModelProcessor,
    ) = AppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
