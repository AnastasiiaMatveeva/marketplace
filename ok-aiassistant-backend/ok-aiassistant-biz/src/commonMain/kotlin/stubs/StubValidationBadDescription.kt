package ru.otus.otuskotlin.aiassistant.biz.stubs

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import models.AIError
import stubs.AIStubs
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для описания
    """.trimIndent()
    on { stubCase == AIStubs.BAD_DESCRIPTION && state == AIState.RUNNING }
    handle {
        fail(
            AIError(
                group = "validation",
                code = "validation.validation-position",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}
