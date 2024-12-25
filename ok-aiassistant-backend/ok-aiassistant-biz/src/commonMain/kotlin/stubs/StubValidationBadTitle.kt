package ru.otus.otuskotlin.aiassistant.biz.stubs

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import models.AIError
import stubs.AIStubs
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для title
    """.trimIndent()

    on { stubCase == AIStubs.BAD_TITLE && state == AIState.RUNNING }
    handle {
        fail(
            AIError(
                group = "validation",
                code = "validation.validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}
