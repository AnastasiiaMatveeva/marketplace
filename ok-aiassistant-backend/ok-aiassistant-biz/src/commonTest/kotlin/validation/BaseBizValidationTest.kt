package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.repo.common.ModelRepoInitialized
import models.AICommand
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import ru.otus.otuskotlin.aiassistant.repo.inmemory.ModelRepoInMemory

abstract class BaseBizValidationTest {
    protected abstract val command: AICommand
    private val repo = ModelRepoInitialized(
        repo = ModelRepoInMemory(),
        initObjects = listOf(
            ModelStub.get(),
        ),
    )
    private val settings by lazy { CorSettings(repoTest = repo) }
    protected val processor by lazy { ModelProcessor(settings) }
}
