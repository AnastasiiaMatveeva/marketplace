package ru.otus.otuskotlin.aiassistant.app.spring.repo

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel
import ru.otus.otuskotlin.aiassistant.repo.inmemory.ModelRepoInMemory

@TestConfiguration
class RepoInMemoryConfig {
    @Suppress("unused")
    @Bean()
    @Primary
    fun prodRepo(): IRepoModel = ModelRepoInMemory()
}
