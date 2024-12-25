package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import models.AICommand
import ru.otus.otuskotlin.aiassistant.common.CorSettings


abstract class BaseBizValidationTest {
    protected abstract val command: AICommand
    private val settings by lazy { CorSettings() }
    protected val processor by lazy { ModelProcessor(settings) }
}
