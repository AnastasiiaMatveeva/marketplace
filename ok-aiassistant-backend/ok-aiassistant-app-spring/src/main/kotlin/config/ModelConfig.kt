package ru.otus.otuskotlin.aiassistant.app.spring.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import ru.otus.otuskotlin.aiassistant.logging.common.LoggerProvider
import ru.otus.otuskotlin.aiassistant.logging.jvm.mpLoggerLogback
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel
import ru.otus.otuskotlin.aiassistant.repo.inmemory.ModelRepoInMemory
import ru.otus.otuskotlin.aiassistant.repo.stubs.ModelRepoStub
import ru.otus.otuskotlin.aiassistant.repo.postgresql.RepoModelSql
import ru.otus.otuskotlin.aiassistant.repo.postgresql.SqlProperties
import com.benasher44.uuid.uuid4

@Suppress("unused")
@EnableConfigurationProperties(ModelConfigPostgres::class)
@Configuration
class ModelConfig(val postgresConfig: ModelConfigPostgres)  {
    @Bean
    fun processor(corSettings: CorSettings) = ModelProcessor(corSettings = corSettings)
        val logger: Logger = LoggerFactory.getLogger(ModelConfig::class.java)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoModel = ModelRepoInMemory()

    @Bean
    fun prodRepo(): IRepoModel = RepoModelSql(
        SqlProperties(
            host = "localhost",
            user = "postgres",
            password = "marketplace-pass",
            port = 50158,
        ),
        randomUuid = { uuid4().toString() }
    )

    @Bean
    fun stubRepo(): IRepoModel = ModelRepoStub()

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
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
