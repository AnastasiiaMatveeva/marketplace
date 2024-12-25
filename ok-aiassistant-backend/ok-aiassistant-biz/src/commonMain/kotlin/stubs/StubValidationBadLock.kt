package ru.otus.otuskotlin.aiassistant.biz.stubs

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import models.AIError
import stubs.AIStubs
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.stubValidationBadLock(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для AIModelLock
    """.trimIndent()
    on { stubCase == AIStubs.BAD_LOCK && state == AIState.RUNNING }
    handle {
        fail(
            AIError(
                group = "validation",
                code = "validation-lock",
                field = "lock",
                message = "Wrong lock field"
            )
        )
    }
}
